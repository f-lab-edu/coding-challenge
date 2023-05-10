package code.executor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

import code.domain.Cause;
import code.domain.Lang;
import code.dto.FailedTestResult;
import code.dto.TestCase;
import code.dto.TestCases;
import code.dto.TestResult;
import code.exception.ThrowerResultExecution;
import code.exception.WrongResultException;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
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
                   .flatMap(testCase -> {
                       final var testResult = execute(testCase);
                       if (testResult instanceof FailedTestResult failedTestResult && failedTestResult
                               .getCause().equals(Cause.WRONG_ANSWER)) {
                           return Mono.error(new WrongResultException(failedTestResult.getMessage()));
                       }

                       if (testResult instanceof FailedTestResult failedTestResult && failedTestResult
                               .getCause().equals(Cause.ERROR)) {
                           return Mono.error(new ThrowerResultExecution(failedTestResult.getMessage()));
                       }

                       return Mono.just(testResult);
                   })
                   .collectList()
                   .map(TestResult::extract)
                   .onErrorResume(WrongResultException.class,
                                  e -> Mono.just(FailedTestResult.wrong(e.getMessage())))
                   .onErrorResume(ThrowerResultExecution.class,
                                  e -> Mono.just(FailedTestResult.error(e.getMessage())))
                   .publishOn(Schedulers.boundedElastic())
                   .doFinally(signal -> {
                       try {
                           Files.delete(path);
                       } catch (IOException e) {
                           log.warn("파일이 정상적으로 삭제되지 않았습니다. PATH : {}", path);
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
        try {
            final var commands = new String[] { "/bin/sh", "-c", getCommand(path) };
            final var exec = Runtime.getRuntime().exec(commands);
            Objects.requireNonNull(exec);
            input(input, exec);
            exec.waitFor();
            if (exec.exitValue() == 0) {
                return getOutputMessage(exec);
            }

            return getErrorMessage(exec);
        } catch (IOException e) {
            throw new RuntimeException("프로그램 인스턴스화 실패했습니다.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void input(String input, Process exec) {
        if (input != null && !input.isBlank()) {
            try (var outputStream = exec.getOutputStream()) {
                outputStream.write(input.getBytes(StandardCharsets.UTF_8));
                outputStream.write(new byte[] { (byte) '\n' });
            } catch (IOException e) {
                throw new RuntimeException("프로그램 입력에 실패했습니다.");
            }
        }
    }

    private String getErrorMessage(Process exec) {
        final var builder = new StringBuilder();
        try (var reader = exec.errorReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            return builder.toString().trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            exec.destroy();
        }
    }

    private String getOutputMessage(Process exec) {
        final var builder = new StringBuilder();
        try (var reader = exec.inputReader()) {
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
        final var tmp = Paths.get(DIRECTORY).resolve(getFilename(uuid));
        if (Files.exists(tmp)) {
            throw new IllegalArgumentException("이미 존재합니다.");
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
