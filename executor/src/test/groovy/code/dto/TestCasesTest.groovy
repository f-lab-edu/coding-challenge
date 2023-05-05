package code.dto

import code.exception.InvalidTestCasesException
import spock.lang.Specification

class TestCasesTest extends Specification {

    //C39
    def "테스트 케이스를 생성한다."() {
        when:
        def testCases = new TestCases(Arrays.asList(new TestCase("", "Hello World!")))

        then:
        assert testCases.testCases().size() == 1
    }

    //C40
    def "테스트 케이스 리스트(`TestCases`)는 수정하면 `UnsupportedOperationException` 예외가 발생한다"() {
        given:
        def testCases = new TestCases(Arrays.asList(new TestCase("", "Hello World!")))

        when:
        testCases.testCases().add(new TestCase("", "Hello World!"))

        then:
        thrown UnsupportedOperationException
    }

    //C41
    def "테스트 케이스 리스트(`TestCases`)를 생성할 때, 테스트 케이스 리스트가 없거나 비어있으면 `InvalidTestCasesException` 예외가 발생한다"() {
        when:
        new TestCases(testCases)

        then:
        thrown exception

        where:
        testCases | exception
        null      | InvalidTestCasesException
        []        | InvalidTestCasesException
    }
}
