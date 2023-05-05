package code.exception;

public final class InvalidTestCasesException extends RuntimeException {

    private InvalidTestCasesException(String message) {
        super(message);
    }

    public static InvalidTestCasesException invalidTestCases() {
        return new InvalidTestCasesException("유효하지 않은 테스트 케이스 목록");
    }
}
