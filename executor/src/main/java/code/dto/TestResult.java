package code.dto;

import java.util.List;
import java.util.Objects;

import code.domain.Cause;

public class TestResult {
    private static final String EXCEPTION = "EXCEPTION";
    private static final String ERROR = "ERROR";
    private final Boolean isSucceeded;

    TestResult(Boolean isSucceeded) {
        this.isSucceeded = isSucceeded;
    }

    public static TestResult of(String message, String output) {
        if (isException(message)) {
            return new FailedTestResult(false, Cause.ERROR, extractCause(message));
        }

        if (isCorrect(message, output)) {
            return new SucceededTestResult(true, extractExecutionTime(message), extractMemoryUsage(message));
        }

        return new FailedTestResult(false, Cause.WRONG_ANSWER, "WRONG_ANSWER");
    }

    public Boolean isSucceeded() {
        return isSucceeded;
    }

    public static TestResult extract(List<TestResult> results) {
        final var totalResult = (SucceededTestResult) results.stream().reduce(TestResult::extract).get();
        return new SucceededTestResult(true, totalResult.getExecutionTime(),
                                       totalResult.getMemoryUsage() / results.size());
    }

    private TestResult extract(TestResult nextResult) {
        if (this instanceof FailedTestResult || nextResult instanceof FailedTestResult) {
            throw new IllegalArgumentException();
        }

        final var nowResult = (SucceededTestResult) this;
        final var nextSucceededResult = (SucceededTestResult) nextResult;

        return new SucceededTestResult(
                true,
                nowResult.getExecutionTime() + nextSucceededResult.getExecutionTime(),
                (nowResult.getMemoryUsage() + nextSucceededResult.getMemoryUsage())
        );
    }

    private static boolean isException(String message) {
        return message.toUpperCase().contains(EXCEPTION) || message.toUpperCase().contains(ERROR);
    }

    private static boolean isCorrect(String message, String output) {
        return Objects.equals(extractOutput(message), output);
    }

    private static String extractCause(String message) {
        return message;
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
