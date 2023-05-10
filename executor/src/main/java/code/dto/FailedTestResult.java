package code.dto;

import code.domain.Cause;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
public final class FailedTestResult extends TestResult {
    @ToString.Include
    private final Cause cause;
    @ToString.Include
    private final String message;

    public FailedTestResult(Boolean isSucceeded, Cause cause, String message) {
        super(isSucceeded);
        this.cause = cause;
        this.message = message;
    }

    public static FailedTestResult wrong(String message) {
        return new FailedTestResult(false, Cause.WRONG_ANSWER, message);
    }

    public static FailedTestResult error(String message) {
        return new FailedTestResult(false, Cause.ERROR, message);
    }
}
