package code.domain


import code.exception.InvalidSucceededResultException
import spock.lang.Specification

import java.time.LocalDateTime

class SucceededResultTest extends Specification {
    def static final QUESTION_ID = UUID.randomUUID().toString()
    def static final USER_ID = UUID.randomUUID().toString()
    def static final CODE = new UserCode(Lang.PYTHON3, """
                print("Hello World!")
        """)

    //C31
    def "성공한 테스트 결과를 생성한다"() {
        when:
        def result = new SucceededResult(QUESTION_ID, USER_ID, CODE, true, LocalDateTime.now(),
                39020, 3120353273)
        then:
        assert result.getUserCode() == CODE
        assert result.getQuestionId() == QUESTION_ID
        assert result.getUserId() == USER_ID
        assert result.isSucceed == true
        assert result.createdAt != null
    }

    //C33
    def "성공한 테스트 결과를 생성할 때, 총 실행 시간(`TotalExecutionTime`)이 없거 음수면 `InvalidSucceededResultException` 예외가 발생한다"() {
        when:
        new SucceededResult(QUESTION_ID, USER_ID, CODE, true, LocalDateTime.now(),
                totalExecutionTime, 3120353273)
        then:
        thrown(InvalidSucceededResultException)

        where:
        totalExecutionTime << [-1, -100, -1000, null]
    }

    //C35
    def "성공한 테스트 결과를 생성할 때, 사용한 메모리 평균치 정보(`AverageUsedMemeory`)는 없거나 음수면 `InvalidSucceededResultException` 예외가 발생한다"() {
        when:
        new SucceededResult(QUESTION_ID, USER_ID, CODE, true, LocalDateTime.now(),
                3000, averageMemoryUsage)
        then:
        thrown(InvalidSucceededResultException)

        where:
        averageMemoryUsage << [-1, -100, -1000, null]
    }
}
