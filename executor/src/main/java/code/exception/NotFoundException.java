package code.exception;

public final class NotFoundException extends RuntimeException {

    private static final String QUESTION = "Question";
    private static final String MEMBER = "Member";
    private static final String RESULT = "Result";
    private static final String MESSAGE_FORMAT = "식별자가 %s인 %s를 찾을 수 없습니다.";

    private NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException notFoundQuestion(String questionId) {
        return new NotFoundException(String.format(MESSAGE_FORMAT, questionId, QUESTION));
    }

    public static NotFoundException notFoundMember(String memberId) {
        return new NotFoundException(String.format(MESSAGE_FORMAT, memberId, MEMBER));
    }

    public static NotFoundException notFoundResult(String resultId) {
        return new NotFoundException(String.format(MESSAGE_FORMAT, resultId, RESULT));
    }
}
