package code.dto

import code.domain.Lang
import code.domain.UserCode
import code.exception.InvalidCodeTypeException
import spock.lang.Specification

class UserCodeTest extends Specification {
    private static final String JAVA_CODE = """
        public class Main {
                public static void main(String[] args) {
                    System.out.println("Hello World!");
                }
        }"""
    private static final String PYTHON_CODE = """
        print("Hello World!")
    """

    //C24
    def "코드를 생성한다"() {
        when:
        def userCode = new UserCode(lang, code)

        then:
        assert userCode.lang() == lang
        assert userCode.code() == code

        where:
        lang         | code
        Lang.JAVA11  | JAVA_CODE
        Lang.PYTHON3 | PYTHON_CODE
    }

    //C25
    def "코드를 생성할 때, 코드 타입(`Lang`)이 없으면 `InvalidCodeTypeException` 예외가 발생한다"() {
        when:
        new UserCode(null, code)

        then:
        thrown(InvalidCodeTypeException)

        where:
        code << [JAVA_CODE, PYTHON_CODE]
    }

    //C26
    def "코드를 생성할 때, 코드(`code`)가 없으면 `InvalidCodeTypeException` 예외가 발생한다"() {
        when:
        new UserCode(lang, null)

        then:
        thrown(InvalidCodeTypeException)

        where:
        lang << [Lang.JAVA11, Lang.PYTHON3]
    }
}
