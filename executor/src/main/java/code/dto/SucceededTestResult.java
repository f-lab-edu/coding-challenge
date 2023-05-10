package code.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
public final class SucceededTestResult extends TestResult {
    @ToString.Include
    private final Long executionTime;
    @ToString.Include
    private final Long memoryUsage;

    public SucceededTestResult(Boolean isSucceeded, Long executionTime, Long memoryUsage) {
        super(isSucceeded);
        this.executionTime = executionTime;
        this.memoryUsage = memoryUsage;
    }
}
