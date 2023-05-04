package code.domain;

import org.springframework.stereotype.Service;

import code.dto.Code;
import code.dto.TestCases;
import code.dto.TestResult;
import reactor.core.publisher.Mono;

@Service
public interface ExecutorService {

    Mono<TestResult> executeCode(Code code, TestCases testCases);
}
