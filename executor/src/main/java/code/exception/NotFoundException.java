package code.exception;

public final class NotFoundException extends RuntimeException {

    private static final String QUESTION = "Question";
    private static final String MEMBER = "Member";

    private NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException notFoundQuestion(String questionId) {
        return new NotFoundException(String.format("%s를 찾을 수 없습니다. 식별는 %s 입니다.", QUESTION, questionId));
    }

    public static NotFoundException notFoundMember(String memberId) {
        return new NotFoundException(String.format("%s를 찾을 수 없습니다. 식별는 %s 입니다.", MEMBER, memberId));
    }
}
