package code.application

import code.domain.*
import code.dto.*
import code.exception.ExecutionException
import code.exception.NotFoundException
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

import java.time.LocalDateTime

class ExecutionServiceTest extends Specification {

    def CODE = """
                    public class Main {
                        public static void main(String[] args) {
                            System.out.println("Hello World!");
                        }
                    }
                """
    def JAVA11 = "java11"
    def MEMBER_ID = "this-is-user-id"
    def QUESTION_ID = UUID.randomUUID().toString()

    def executorService = Mock(ExecutorService)
    def memberRepository = Mock(MemberRepository)
    def questionRepository = Mock(QuestionRepository)
    def executionResultRepository = Mock(ExecutionResultRepository)
    def executionService = new ExecutionService(executorService, memberRepository, questionRepository, executionResultRepository)

    //C14
    def "코드를 실행하고 성공한 실행 결과를 반환받는다."() {
        given:
        def succeededResult = new SucceededResult(QUESTION_ID, MEMBER_ID, CODE, JAVA11, true, LocalDateTime.now(), 1000L, 234L)
        def succeededTestResult = new SucceededTestResult(true, 1000L, 234L)

        when:
        memberRepository.findById(MEMBER_ID) >> Mono.just(new Member(MEMBER_ID))
        questionRepository.findById(QUESTION_ID) >> Mono.just(new Question(QUESTION_ID, new TestCases(Arrays.asList(new TestCase("", "Hello World!")))))
        executorService.executeCode(_, _) >> Mono.just(succeededTestResult)
        executionResultRepository.save(_) >> Mono.just(succeededResult)

        then:
        StepVerifier.create(executionService.executeCode(MEMBER_ID, QUESTION_ID, new ExecutionRequest(CODE, JAVA11)))
                .consumeNextWith({ executionResult ->
                    assert executionResult.isSucceeded()
                }).verifyComplete()
    }

    //C14
    def "코드를 실행하고 실패한 실행 결과를 반환받는다."() {
        given:
        def succeededResult = new FailedResult(QUESTION_ID, MEMBER_ID, CODE, JAVA11, false, LocalDateTime.now(), Cause.WRONG_ANSWER, "결과 값이 `Hello World`가 아닙니다., 출력된 결과는 `Hello World!`입니다.")
        def succeededTestResult = new FailedTestResult(false, Cause.WRONG_ANSWER, "결과 값이 `Hello World`가 아닙니다., 출력된 결과는 `Hello World!`입니다.")

        when:
        memberRepository.findById(MEMBER_ID) >> Mono.just(new Member(MEMBER_ID))
        questionRepository.findById(QUESTION_ID) >> Mono.just(new Question(QUESTION_ID, new TestCases(Arrays.asList(new TestCase("", "Hello World!")))))
        executorService.executeCode(_, _) >> Mono.just(succeededTestResult)
        executionResultRepository.save(_) >> Mono.just(succeededResult)

        then:
        StepVerifier.create(executionService.executeCode(MEMBER_ID, QUESTION_ID, new ExecutionRequest(CODE, JAVA11)))
                .consumeNextWith({ executionResult ->
                    assert !executionResult.isSucceeded()
                }).verifyComplete()
    }

    //C15
    def "코드를 실행할 때, 사용자 정보가 없으면 `NotFoundException`예외가 발생한다"() {
        when:
        memberRepository.findById(MEMBER_ID) >> Mono.empty()

        then:
        StepVerifier.create(executionService.executeCode(MEMBER_ID, QUESTION_ID, new ExecutionRequest(CODE, JAVA11)))
                .expectError(NotFoundException.class).verify()
    }

    //C16
    def "코드를 실행할 때, 문제 정보가 없으면 `NotFoundException`예외가 발생한다"() {
        when:
        memberRepository.findById(MEMBER_ID) >> Mono.just(new Member(MEMBER_ID))
        questionRepository.findById(QUESTION_ID) >> Mono.empty()

        then:
        StepVerifier.create(executionService.executeCode(MEMBER_ID, QUESTION_ID, new ExecutionRequest(CODE, JAVA11)))
                .expectError(NotFoundException.class).verify()
    }

    //C51
    def "코드를 실행할 때, `executionCode`에서 반환받은 데이터가 없다면 `NotFoundException` 예외가 발생한다"() {
        when:
        memberRepository.findById(MEMBER_ID) >> Mono.just(new Member(MEMBER_ID))
        questionRepository.findById(QUESTION_ID) >> Mono.just(new Question(QUESTION_ID, new TestCases(Arrays.asList(new TestCase("", "Hello World!")))))
        executorService.executeCode(_, _) >> Mono.empty()

        then:
        StepVerifier.create(executionService.executeCode(MEMBER_ID, QUESTION_ID, new ExecutionRequest(CODE, JAVA11)))
                .expectError(ExecutionException.class).verify()
    }
}
