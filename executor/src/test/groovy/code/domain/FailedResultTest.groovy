package code.domain


import code.exception.InvalidFailedResultException
import spock.lang.Specification

import java.time.LocalDateTime

class FailedResultTest extends Specification {
    private static final String QUESTION_ID = UUID.randomUUID().toString()
    private static final String USER_ID = UUID.randomUUID().toString()
    private static final UserCode CODE = new UserCode(Lang.PYTHON3, """
                print("Hello World!")
        """)

    //C36
    def "실패한 테스트 결과를 생성한다"() {
        when:
        def result = new FailedResult(QUESTION_ID, USER_ID, CODE, false, LocalDateTime.now(),
                Cause.WRONG_ANSWER, "정답이 틀렸습니다.");

        then:
        assert result.getUserCode() == CODE
        assert result.getQuestionId() == QUESTION_ID
        assert result.getUserId() == USER_ID
        assert result.isSucceed == false
        assert result.getCause() == Cause.WRONG_ANSWER
        assert result.getMessage() == "정답이 틀렸습니다."
        assert result.createdAt != null
    }

    //C37
    def "실패한 테스트 결과를 생성할 때, 실패 원인(`Cause`)이 없으면 `InvalidFailedResultException` 예외가 발생한다"() {
        when:
        new FailedResult(QUESTION_ID, USER_ID, CODE, false, LocalDateTime.now(),
                null, "정답이 틀렸습니다.");
        then:
        thrown(InvalidFailedResultException)
    }

    //C38
    def "실패한 테스트 결과를 생성할 때, 실패 정보(`Message`)가 없거나 비어있으면 `InvalidFailedResultException` 예외가 발생한다"() {
        when:
        new FailedResult(QUESTION_ID, USER_ID, CODE, false, LocalDateTime.now(),
                Cause.WRONG_ANSWER, message);
        then:
        thrown(InvalidFailedResultException)

        where:
        message << ["", null]
    }
}
