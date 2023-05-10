package code.dto

import code.domain.Cause
import spock.lang.Specification

class TestResultTest extends Specification {

    //C48
    def "성공한 테스트 결과 두 개를 추합한다"() {
        given:
        def prevSucceededTestResult = new SucceededTestResult(true, 1000L, 1000L);
        def nextSucceededTestResult = new SucceededTestResult(true, 1000L, 1000L);
        def nextNextSucceededTestResult = new SucceededTestResult(true, 2000L, 3000L);
        def list = Arrays.asList(prevSucceededTestResult, nextSucceededTestResult,
                nextNextSucceededTestResult)
        when:
        def result = TestResult.extract(list)

        then:
        assert result instanceof SucceededTestResult
        assert ((SucceededTestResult) result).getExecutionTime() == list.stream()
                .map(SucceededTestResult::getExecutionTime)
                .reduce((r1, r2) -> r1 + r2)
                .get()
        assert ((SucceededTestResult) result).getMemoryUsage() == (int) (list.stream()
                .map(SucceededTestResult::getMemoryUsage)
                .reduce((r1, r2) -> r1 + r2)
                .get() / list.size())
    }

    //C49
    def "실패한 테스트는 추출할 수 없다"() {
        given:
        def prevSucceededTestResult = new SucceededTestResult(true, 1000L, 1000L);
        def nextFailedTestResult = new FailedTestResult(false, Cause.WRONG_ANSWER, "wrong answer 1000L, 1000L");

        when:
        TestResult.extract(Arrays.asList(prevSucceededTestResult, nextFailedTestResult))

        then:
        thrown(IllegalArgumentException)
    }
}
