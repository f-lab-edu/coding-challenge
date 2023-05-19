package code.dto;

import java.util.List;
import java.util.Objects;

import code.domain.Cause;

public class TestResult {
    private final Boolean isSucceeded;

    TestResult(Boolean isSucceeded) {
        this.isSucceeded = isSucceeded;
    }

    public static TestResult of(String message, String output, Long executionTime, Long memoryUsage) {
        if (isCorrect(message, output)) {
            return new SucceededTestResult(true, executionTime, memoryUsage);
        }

        return new FailedTestResult(false, Cause.WRONG_ANSWER, "WRONG_ANSWER");
    }

    public Boolean isSucceeded() {
        return isSucceeded;
    }

    public static TestResult extract(List<TestResult> results) {
        final var totalResult = (SucceededTestResult) results.stream().reduce(TestResult::extract)
                                                             .orElseThrow();
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

    private static boolean isCorrect(String message, String output) {
        return Objects.equals(message, output);
    }
}
