package code.dto

import code.exception.InvalidCodeTypeException
import spock.lang.Specification

class CodeTest extends Specification {

    def static final JAVA11 = "JAVA11"
    def static final JAVA_CODE = """
        public class Main {
                public static void main(String[] args) {
                    System.out.println("Hello World!");
                }
        }"""

    def static final PYTHON3 = "PYTHON3"
    def static final PYTHON_CODE = """
        print("Hello World!")
    """

    //C24
    def "코드를 생성한다"() {
        when:
        def userCode = new Code(lang, code)

        then:
        assert userCode.lang() == lang
        assert userCode.code() == code

        where:
        lang    | code
        Lang.JAVA11  | JAVA_CODE
        Lang.PYTHON3 | PYTHON_CODE
    }

    //C25
    def "코드를 생성할 때, 코드 타입(`Lang`)이 없으면 `InvalidCodeTypeException` 예외가 발생한다"() {
        when:
        new Code(null, code)

        then:
        thrown(InvalidCodeTypeException)

        where:
        code << [JAVA_CODE, PYTHON_CODE]
    }

    //C26
    def "코드를 생성할 때, 코드(`code`)가 없으면 `InvalidCodeTypeException` 예외가 발생한다"() {
        when:
        new Code(lang, null)

        then:
        thrown(InvalidCodeTypeException)

        where:
        lang << [Lang.JAVA11, Lang.PYTHON3]
    }
}
