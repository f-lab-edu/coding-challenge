package code.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import code.domain.Lang;
import code.dto.TestCase;
import code.dto.TestCases;
import code.dto.TestResult;
import jakarta.annotation.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public abstract class AbstractCode {
    private final Path path;
    protected static final String DIRECTORY = "/Users/keonchanglee/Desktop/codingtest/";

    public AbstractCode(String uuid, String code) {
        this.path = initializeFile(uuid, code);
    }

    public static AbstractCode of(Lang lang, String code) {
        return switch (lang) {
            case JAVA11 -> new JavaCode(UUID.randomUUID().toString(), code);
            case PYTHON3 -> new PythonCode(UUID.randomUUID().toString(), code);
        };
    }

    public Mono<TestResult> execute(TestCases testCases) {
        return Flux.fromIterable(testCases.testCases())
                   .map(this::execute)
                   .reduce(TestResult::extract)
                   .publishOn(Schedulers.boundedElastic())
                   .doFinally(signal -> {
                       try {
                           Files.delete(path);
                       } catch (IOException e) {
                           throw new RuntimeException("파일 삭제에 실패했습니다.");
                       }
                   });
    }

    protected abstract String getCommand(Path path);

    protected abstract String getFilename(String uuid);

    private TestResult execute(final TestCase testCase) {
        final var message = execute(testCase.input());
        return TestResult.of(message, testCase.output());
    }

    private String execute(@Nullable String input) {
        final var commands = new String[] { "/bin/sh", "-c", getCommand(path) };
        Process exec;
        try {
            exec = Runtime.getRuntime().exec(commands);
        } catch (IOException e) {
            throw new RuntimeException("프로그램 인스턴스화 실패했습니다.");
        }

        Objects.requireNonNull(exec);

        if (input != null && !input.isBlank()) {
            try (var outputStream = exec.getOutputStream()) {
                outputStream.write(input.getBytes(StandardCharsets.UTF_8));
                outputStream.write(new byte[] { (byte) '\n' });
            } catch (IOException e) {
                throw new RuntimeException("프로그램 입력에 실패했습니다.");
            }
        }

        var builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException("출력 결과를 읽기 데 실패했습니다.");
        }

        return builder.toString().trim();
    }

    private Path initializeFile(String uuid, String code) {
        var tmp = Paths.get(DIRECTORY).resolve(getFilename(uuid));
        if (Files.exists(tmp)) {
            throw new IllegalArgumentException("이미 존재");
        }

        try (var writer = Files.newBufferedWriter(tmp)) {
            writer.write(code);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("명령어를 실행하기 위해 초기화하는 과정이 실패했습니다.");
        }

        return tmp;
    }

}
