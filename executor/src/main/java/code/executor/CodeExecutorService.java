package code.executor;

import org.springframework.stereotype.Service;

import code.domain.ExecutorService;
import code.domain.UserCode;
import code.dto.TestCases;
import code.dto.TestResult;
import reactor.core.publisher.Mono;

@Service
public class CodeExecutorService implements ExecutorService {
    @Override
    public Mono<TestResult> executeCode(UserCode userCode, TestCases testCases) {
        return AbstractCode.of(userCode.lang(), userCode.code()).execute(testCases);
    }
}
