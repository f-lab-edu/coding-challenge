package code.executor

import code.domain.Lang
import code.domain.UserCode
import code.dto.SucceededTestResult
import code.dto.TestCase
import code.dto.TestCases
import reactor.test.StepVerifier
import spock.lang.Specification

class CodeExecutorServiceTest extends Specification {
    private static final CodeExecutorService codeExecutorService = new CodeExecutorService()
    private static final String JAVA_CODE = """
        public class Main {
                public static void main(String[] args) {
                    System.out.println("Hello World!");
                }
        }"""

    //C47
    def "코드(`Code`)를 실행한다"() {
        when:
        StepVerifier.create(codeExecutorService.executeCode(new UserCode(Lang.JAVA11, JAVA_CODE),
                new TestCases(Arrays.asList(new TestCase("", "Hello World!")))))
                .consumeNextWith({ result ->
                    assert result instanceof SucceededTestResult
                    assert result.isSucceeded() == true
                })
                .verifyComplete()
        then:
        true
    }
}
