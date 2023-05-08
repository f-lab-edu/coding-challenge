package code.domain


import spock.lang.Specification

import java.time.LocalDateTime

class ExecutionResultTest extends Specification {
    def static final QUESTION_ID = UUID.randomUUID().toString()
    def static final USER_ID = UUID.randomUUID().toString()
    def static final CODE = new UserCode(Lang.PYTHON3, """
                print("Hello World!")
        """)

    //C27
    def "테스트 결과를 생성한다"() {

        when:
        def executionResult = new ExecutionResult(QUESTION_ID, USER_ID, CODE, true, LocalDateTime.now());

        then:
        assert executionResult.getUserCode() == CODE
        assert executionResult.getQuestionId() == QUESTION_ID
        assert executionResult.getUserId() == USER_ID
        assert executionResult.getIsSucceed()
        assert executionResult.getCreatedAt() != null
    }

    //C28
    def "테스트 결과를 생성할 때, 코드(`Code`)가 없으면 `IllegalArgumentException` 예외가 발생한다"() {
        when:
        new ExecutionResult(QUESTION_ID, USER_ID, null, true, LocalDateTime.now());

        then:
        thrown(IllegalArgumentException)
    }

    //C29
    def "테스트 결과를 생성할 때, 문제 식별자(`QuestionId`)가 없거나 비어있으면 `IllegalArgumentException` 예외가 발생한다"() {
        when:
        new ExecutionResult(null, USER_ID, CODE, true, LocalDateTime.now());

        then:
        thrown(IllegalArgumentException)
    }

    //C30
    def "테스트 결과를 생성할 때, 사용자 식별자(`UserId`)가 없거나 비어있으면 `IllegalArgumentException` 예외가 발생한다"() {
        when:
        new ExecutionResult(QUESTION_ID, null, CODE, true, LocalDateTime.now());

        then:
        thrown(IllegalArgumentException)
    }
}
