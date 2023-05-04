package code.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
public final class SucceededResult extends ExecutionResult {
    @ToString.Include
    private final Long totalExecutionTime;
    @ToString.Include
    private final Long averageMemoryUsage;

    public SucceededResult(String questionId, String userId, String code, String lang, Boolean isSucceed,
                           LocalDateTime createdAt, Long totalExecutionTime, Long averageMemoryUsage) {
        super(questionId, userId, code, lang, isSucceed, createdAt);
        this.totalExecutionTime = totalExecutionTime;
        this.averageMemoryUsage = averageMemoryUsage;
    }
}
