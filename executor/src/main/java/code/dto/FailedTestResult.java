package code.dto;

import code.domain.Cause;
import lombok.Getter;

@Getter
public final class FailedTestResult extends TestResult {
    private final Cause cause;
    private final String message;

    public FailedTestResult(Boolean isSucceeded, Cause cause, String message) {
        super(isSucceeded);
        this.cause = cause;
        this.message = message;
    }
}
