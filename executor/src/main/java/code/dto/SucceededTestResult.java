package code.dto;

import lombok.Getter;

@Getter
public final class SucceededTestResult extends TestResult {
    private final Long totalExecutionTime;
    private final Long averageMemoryUsage;

    public SucceededTestResult(Boolean isSucceeded, Long totalExecutionTime, Long averageMemoryUsage) {
        super(isSucceeded);
        this.totalExecutionTime = totalExecutionTime;
        this.averageMemoryUsage = averageMemoryUsage;
    }
}
