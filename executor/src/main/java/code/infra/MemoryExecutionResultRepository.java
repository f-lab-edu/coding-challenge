package code.infra;

import org.springframework.stereotype.Repository;

import code.domain.ExecutionResult;
import code.domain.ExecutionResultRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MemoryExecutionResultRepository implements ExecutionResultRepository {
    @Override
    public Mono<ExecutionResult> save(ExecutionResult executionResult) {
        return null;
    }

    @Override
    public Mono<ExecutionResult> findById(String resultId) {
        return null;
    }

    @Override
    public Flux<ExecutionResult> findAllByMemberIdAndQuestionId(String memberId, String questionId) {
        return null;
    }
}
