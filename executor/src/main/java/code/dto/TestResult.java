package code.dto;

import java.util.Objects;

import code.domain.Cause;

public class TestResult {
    private final Boolean isSucceeded;

    TestResult(Boolean isSucceeded) {
        this.isSucceeded = isSucceeded;
    }

    public static TestResult of(String message, String output) {
        if (message.contains("Exception")) {
            return new FailedTestResult(false, Cause.ERROR, extractCause(message));
        } else if (Objects.equals(output, extractOutput(message))) {
            return new SucceededTestResult(true, extractExecutionTime(message), extractMemoryUsage(message));
        }
        return new FailedTestResult(false, Cause.WRONG_ANSWER, "");
    }

    public Boolean isSucceeded() {
        return isSucceeded;
    }

    public static TestResult extract(TestResult nowResult, TestResult nextResult) {
        if (nextResult instanceof FailedTestResult) {
            return nextResult;
        }
        return nowResult.extract(nextResult);
    }

    private TestResult extract(TestResult nextResult) {
        SucceededTestResult nowResult = (SucceededTestResult) this;
        SucceededTestResult nextSucceededResult = (SucceededTestResult) nextResult;
        return new SucceededTestResult(
                true,
                nowResult.getExecutionTime() + nextSucceededResult.getExecutionTime(),
                (nowResult.getMemoryUsage() + nextSucceededResult.getMemoryUsage()) / 2
        );
    }

    private static String extractCause(String message) {
        return "";
    }

    private static String extractOutput(String message) {
        return message;
    }

    private static Long extractMemoryUsage(String message) {
        return 0L;
    }

    private static Long extractExecutionTime(String message) {
        return 0L;
    }
}
