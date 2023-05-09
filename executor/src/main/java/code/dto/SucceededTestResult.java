package code.dto;

import lombok.Getter;

@Getter
public final class SucceededTestResult extends TestResult {
    private final Long executionTime;
    private final Long memoryUsage;

    public SucceededTestResult(Boolean isSucceeded, Long executionTime, Long memoryUsage) {
        super(isSucceeded);
        this.executionTime = executionTime;
        this.memoryUsage = memoryUsage;
    }
}
