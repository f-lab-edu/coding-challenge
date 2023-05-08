package code.domain;

import code.dto.TestCases;
import code.dto.TestResult;
import reactor.core.publisher.Mono;

public interface ExecutorService {

    Mono<TestResult> executeCode(UserCode userCode, TestCases testCases);
}
