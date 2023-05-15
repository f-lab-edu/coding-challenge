package code.executor

import code.domain.Cause
import code.domain.Lang
import code.dto.FailedTestResult
import code.dto.SucceededTestResult
import code.dto.TestCase
import code.dto.TestCases
import reactor.test.StepVerifier
import spock.lang.Specification

class CodeTest extends Specification {
    private static final String JAVA_CODE = """
        public class Main {
            public static void main(String[] args) {
                System.out.println("Hello World!");
            }
        }
        """
    private static final String ERROR_JAVA_CODE = """
        public class Main {
            public static void main(String[] args) {
                System.out.println(0/0);
            }
        }
        """
    private static final String JAVA_CODE_FOR_INPUT = """
        import java.io.IOException;
        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        public class Main {
            public static void main(String[] args) throws IOException {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.println(br.readLine());
            }
        }
        """
    private static final String PYTHON_CODE = "print(\"Hello World!\")"
    private static final String ERROR_PYTHON_CODE = "print(0/0)"
    private static final String PYTHON_CODE_FOR_INPUT = "import sys\nprint(sys.stdin.readline())"
    private static final TestCase WRONG_TESTCASE = new TestCase(null, "Hello World")
    private static final TestCase CORRECT_TESTCASE = new TestCase(null, "Hello World!")

    //C45
    def "자바 코드와 파이썬 코드를 생성한다"() {
        when:
        def userCode = AbstractCode.of(lang, code);

        then:
        assert userCode.class == classType

        where:
        lang         | code        | classType
        Lang.PYTHON3 | PYTHON_CODE | PythonCode.class
        Lang.JAVA11  | JAVA_CODE   | JavaCode.class
    }

    //C47
    def "코드를 실행해 성공한 결과를 반환한다"() {
        given:
        def userCode = AbstractCode.of(lang, code);
        def testCases = new TestCases(Arrays.asList(CORRECT_TESTCASE))

        expect:
        StepVerifier.create(userCode.execute(testCases))
                .consumeNextWith({ result ->
                    assert result instanceof SucceededTestResult
                    assert result.isSucceeded() == true
                }).verifyComplete()

        where:
        lang         | code        | classType
        Lang.PYTHON3 | PYTHON_CODE | PythonCode.class
        Lang.JAVA11  | JAVA_CODE   | JavaCode.class
    }

    //C49
    def "코드를 실행해 실패한 결과를 반환한다"() {
        given:
        def userCode = AbstractCode.of(lang, code);
        def wrongTestCases = new TestCases(Arrays.asList(WRONG_TESTCASE))

        expect:
        StepVerifier.create(userCode.execute(wrongTestCases))
                .consumeNextWith({ result ->
                    assert result instanceof FailedTestResult
                    assert result.isSucceeded() == false
                    assert ((FailedTestResult) result).getCause() == Cause.WRONG_ANSWER
                }).verifyComplete()

        where:
        lang         | code        | classType
        Lang.PYTHON3 | PYTHON_CODE | PythonCode.class
        Lang.JAVA11  | JAVA_CODE   | JavaCode.class
    }

    //C50
    def "코드를 실행할 때 예외가 발생해도 결과를 반환한다"() {
        given:
        def userCode = AbstractCode.of(lang, code);
        def wrongTestCases = new TestCases(Arrays.asList(CORRECT_TESTCASE))

        expect:
        StepVerifier.create(userCode.execute(wrongTestCases))
                .consumeNextWith({ result ->
                    assert result instanceof FailedTestResult
                    assert result.isSucceeded() == false
                    assert ((FailedTestResult) result).getCause() == Cause.ERROR
                }).verifyComplete()

        where:
        lang         | code              | classType
        Lang.PYTHON3 | ERROR_PYTHON_CODE | PythonCode.class
        Lang.JAVA11  | ERROR_JAVA_CODE   | JavaCode.class
    }

    // C46
    def "여러 개의 성공하는 테스트 케이스를 실행한다"() {
        given:
        def userCode = AbstractCode.of(lang, code);
        def testCases = new TestCases(Arrays.asList(CORRECT_TESTCASE, CORRECT_TESTCASE))

        expect:
        StepVerifier.create(userCode.execute(testCases))
                .consumeNextWith({ result ->
                    assert result instanceof SucceededTestResult
                    assert result.isSucceeded() == true
                }).verifyComplete()

        where:
        lang         | code        | classType
        Lang.PYTHON3 | PYTHON_CODE | PythonCode.class
        Lang.JAVA11  | JAVA_CODE   | JavaCode.class
    }

    // C46
    def "일부 실패하는 테스트 케이스를 넣어 실행한다"() {
        given:
        def userCode = AbstractCode.of(lang, code);
        def testCases = new TestCases(Arrays.asList(WRONG_TESTCASE, CORRECT_TESTCASE))

        expect:
        StepVerifier.create(userCode.execute(testCases))
                .consumeNextWith({ result ->
                    assert result instanceof FailedTestResult
                    assert result.isSucceeded() == false
                    assert ((FailedTestResult) result).getCause() == Cause.WRONG_ANSWER
                }).verifyComplete()

        where:
        lang         | code        | classType
        Lang.PYTHON3 | PYTHON_CODE | PythonCode.class
        Lang.JAVA11  | JAVA_CODE   | JavaCode.class
    }

    // C32
    def "입력 값을 받아 결과 값을 반환한다"() {
        given:
        def userCode = AbstractCode.of(lang, code);
        def testCases = new TestCases(Arrays.asList(new TestCase("Hello World!", "Hello World!")))

        expect:
        StepVerifier.create(userCode.execute(testCases))
                .consumeNextWith({ result ->
                    assert result instanceof SucceededTestResult
                    assert result.isSucceeded() == true
                }).verifyComplete()

        where:
        lang         | code                  | classType
        Lang.PYTHON3 | PYTHON_CODE_FOR_INPUT | PythonCode.class
        Lang.JAVA11  | JAVA_CODE_FOR_INPUT   | JavaCode.class
    }
}
