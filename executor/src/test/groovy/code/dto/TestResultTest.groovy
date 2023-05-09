package code.dto

import code.domain.Cause
import spock.lang.Specification

class TestResultTest extends Specification {

    //C48
    def "성공한 테스트 결과 두 개를 추합한다"() {
        given:
        def prevSucceededTestResult = new SucceededTestResult(true, 1000L, 1000L);
        def nextSucceededTestResult = new SucceededTestResult(true, 1000L, 1000L);

        when:
        def result = TestResult.extract(prevSucceededTestResult, nextSucceededTestResult)

        then:
        assert result instanceof SucceededTestResult
        assert ((SucceededTestResult)result).getExecutionTime() == 2000L
        assert ((SucceededTestResult)result).getMemoryUsage() == 1000L
    }

    //C49
    def "실패한 테스트를 추출하면 실패한 테스트가 생성된다"() {
        given:
        def prevSucceededTestResult = new SucceededTestResult(true, 1000L, 1000L);
        def nextFailedTestResult = new FailedTestResult(false, Cause.WRONG_ANSWER, "wrong answer 1000L, 1000L");

        when:
        def result = TestResult.extract(prevSucceededTestResult, nextFailedTestResult)

        then:
        assert result instanceof FailedTestResult
    }
}
