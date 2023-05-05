package code.executor;

import org.springframework.stereotype.Service;

import code.domain.ExecutorService;
import code.dto.Code;
import code.dto.TestCases;
import code.dto.TestResult;
import reactor.core.publisher.Mono;

@Service
public class CodeExecutorService implements ExecutorService {
    @Override
    public Mono<TestResult> executeCode(Code code, TestCases testCases) {
        return null;
    }
}
