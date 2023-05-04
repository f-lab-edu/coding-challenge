package code.exception;

public final class ExecutionException extends RuntimeException {
    private ExecutionException(String message) {
        super(message);
    }

    public static ExecutionException executeFailed() {
        return new ExecutionException("테스트 실행 결과를 받아올 수 없습니다. 문제가 발생했습니다.");
    }
}
