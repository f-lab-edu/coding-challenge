package code.exception;

public final class InvalidSucceededResultException extends RuntimeException {
    private InvalidSucceededResultException(String message) {
        super(message);
    }

    public static InvalidSucceededResultException invalidTotalExecutionTime() {
        return new InvalidSucceededResultException("TotalExecutionTime is invalid");
    }

    public static InvalidSucceededResultException invalidAverageMemoryUsage() {
        return new InvalidSucceededResultException("AverageMemoryUsage is invalid");
    }
}
