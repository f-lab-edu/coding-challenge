package code.exception;

public final class InvalidTestCasesException extends RuntimeException {

    private InvalidTestCasesException(String message) {
        super(message);
    }

    public static InvalidTestCasesException invalidTestCases() {
        return new InvalidTestCasesException("테스트 케이스가 유효하지 않습니다.");
    }
}
