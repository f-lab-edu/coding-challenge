package code.infra;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import code.domain.ExecutionResult;
import code.domain.ExecutionResultRepository;
import code.domain.FailedResult;
import code.domain.SucceededResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MemoryExecutionResultRepository implements ExecutionResultRepository {
    private final LinkedHashMap<String, ExecutionResult> map = new LinkedHashMap<>();

    @Override
    public Mono<ExecutionResult> save(ExecutionResult executionResult) {
        adjustMaximumCapacity();
        if (executionResult.getId() != null) {
            map.put(executionResult.getId(), executionResult);
            return Mono.just(executionResult);
        }

        if (executionResult instanceof SucceededResult succeededResult) {
            var newSucceededResult = new SucceededResult(UUID.randomUUID().toString(),
                                                         succeededResult.getQuestionId(),
                                                         succeededResult.getUserId(),
                                                         succeededResult.getUserCode(), true,
                                                         succeededResult.getCreatedAt(),
                                                         succeededResult.getTotalExecutionTime(),
                                                         succeededResult.getAverageMemoryUsage());
            map.put(newSucceededResult.getId(), newSucceededResult);
            return Mono.just(newSucceededResult);
        }

        if (executionResult instanceof FailedResult failedResult) {
            var newFailedResult = new FailedResult(UUID.randomUUID().toString(),
                                                   failedResult.getQuestionId(),
                                                   failedResult.getUserId(),
                                                   failedResult.getUserCode(), false,
                                                   failedResult.getCreatedAt(),
                                                   failedResult.getCause(),
                                                   failedResult.getMessage());
            map.put(newFailedResult.getId(), newFailedResult);
            return Mono.just(newFailedResult);
        }

        return Mono.error(new IllegalArgumentException("Unknown type of execution result"));
    }

    @Override
    public Mono<ExecutionResult> findById(String resultId) {
        return Mono.just(map.get(resultId));
    }

    @Override
    public Flux<ExecutionResult> findAllByMemberIdAndQuestionId(String memberId, String questionId) {
        return Flux.fromIterable(map.values()
                                    .stream()
                                    .filter(result -> result.getUserId().equals(memberId)
                                                      && result.getQuestionId()
                                                               .equals(questionId))
                                    .toList());
    }

    public Mono<Void> deleteAll() {
        return Mono.fromRunnable(map::clear);
    }

    private void adjustMaximumCapacity() {
        if (map.size() > 200) {
            String id = map.values().stream()
                           .findFirst()
                           .get().getId();
            map.remove(id);
        }
    }
}
