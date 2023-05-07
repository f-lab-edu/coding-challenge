package code.exception;

public final class InvalidFailedResultException extends RuntimeException {
    private InvalidFailedResultException(String message) {
        super(message);
    }

    public static InvalidFailedResultException invalidCause() {
        return new InvalidFailedResultException("Cause must not be null");
    }

    public static InvalidFailedResultException invalidMessage() {
        return new InvalidFailedResultException("Message must not be null");
    }
}
