package code.domain;

import java.time.LocalDateTime;

import code.dto.Code;
import code.exception.InvalidSucceededResultException;
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
        ensureTotalExecutionTime(totalExecutionTime);
        ensureAverageMemoryUsage(averageMemoryUsage);
        this.totalExecutionTime = totalExecutionTime;
        this.averageMemoryUsage = averageMemoryUsage;
    }

    private void ensureAverageMemoryUsage(Long averageMemoryUsage) {
        if (averageMemoryUsage == null || averageMemoryUsage < 0) {
            throw InvalidSucceededResultException.invalidAverageMemoryUsage();
        }
    }

    private void ensureTotalExecutionTime(Long totalExecutionTime) {
        if (totalExecutionTime == null || totalExecutionTime < 0) {
            throw InvalidSucceededResultException.invalidTotalExecutionTime();
        }
    }
}
