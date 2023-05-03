package code.application;

import org.springframework.stereotype.Service;

import code.dto.ExecutionRequest;
import code.dto.ExecutionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ExecutionService {
    public Mono<ExecutionResponse> executeCode(String userId, String questionId, ExecutionRequest request) {
        return null;
    }

    public Mono<ExecutionResponse> findResult(String userId, String resultId) {
        return null;
    }

    public Flux<ExecutionResponse> findResults(String userId, String questionId) {
        return null;
    }
}
