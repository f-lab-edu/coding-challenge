package code.domain;

import java.time.LocalDateTime;

import code.dto.Code;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
public final class SucceededResult extends ExecutionResult {
    @ToString.Include
    private final Long totalExecutionTime;
    @ToString.Include
    private final Long averageMemoryUsage;

    public SucceededResult(String questionId, String userId, Code code, Boolean isSucceed,
                           LocalDateTime createdAt, Long totalExecutionTime, Long averageMemoryUsage) {
        super(questionId, userId, code, isSucceed, createdAt);
        this.totalExecutionTime = totalExecutionTime;
        this.averageMemoryUsage = averageMemoryUsage;
    }
}
