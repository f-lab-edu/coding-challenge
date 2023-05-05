package code.dto

import code.exception.InvalidTestCaseException
import spock.lang.Specification

class TestCaseTest extends Specification {

    //C42
    def "테스트 케이스를 생성한다"() {
        when:
        def testCases = new TestCase(input, output)

        then:
        testCases.input() == input
        testCases.output() == output

        where:
        input   | output
        "1 2 3" | "4 5 6"
        ""      | "hello world"
    }

    //C43
    def "테스트 케이스를 생성할 때, 테스트 케이스 출력(`output`)이 없거나 비어있으면 `InvalidTestCaseException` 예외가 발생한다"() {
        when:
        new TestCase(input, output)

        then:
        thrown exception

        where:
        input   | output | exception
        "1 2 3" | null   | InvalidTestCaseException
        "1 2 3" | ""     | InvalidTestCaseException
        ""      | ""     | InvalidTestCaseException
    }
}
