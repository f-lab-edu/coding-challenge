package code.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExecutionResultRepository {
    Mono<ExecutionResult> save(ExecutionResult executionResult);

    Mono<ExecutionResult> findById(String resultId);

    Flux<ExecutionResult> findAllByMemberIdAndQuestionId(String memberId, String questionId);
}
