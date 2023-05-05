package code.exception;

public final class InvalidTestCaseException extends RuntimeException {
    public InvalidTestCaseException(String message) {
        super(message);
    }

    public static InvalidTestCaseException invalidTestCase() {
        return new InvalidTestCaseException("유효하지 않은 테스트 케이스");
    }
}
