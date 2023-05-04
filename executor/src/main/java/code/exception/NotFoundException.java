package code.exception;

public final class NotFoundException extends RuntimeException {

    private static final String QUESTION = "Question";
    private static final String MEMBER = "Member";
    private static final String MESSAGE_FORMAT = "%s를 찾을 수 없습니다. 식별는 %s 입니다.";

    private NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException notFoundQuestion(String questionId) {
        return new NotFoundException(String.format(MESSAGE_FORMAT, QUESTION, questionId));
    }

    public static NotFoundException notFoundMember(String memberId) {
        return new NotFoundException(String.format(MESSAGE_FORMAT, MEMBER, memberId));
    }
}
