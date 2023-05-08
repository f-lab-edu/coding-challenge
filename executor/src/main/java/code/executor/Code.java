package code.executor;

import code.domain.Lang;
import code.dto.SucceededTestResult;
import code.dto.TestCases;
import code.dto.TestResult;
import reactor.core.publisher.Mono;

public abstract class Code {
    public static Code of(Lang lang, String code) {
        return switch (lang) {
            case JAVA11 -> new JavaCode(code);
            case PYTHON3 -> new PythonCode(code);
        };
    }

    public Mono<TestResult> execute(TestCases testCases) {
        return Mono.just(new SucceededTestResult(true, 1000L, 2000L));
    }
}
