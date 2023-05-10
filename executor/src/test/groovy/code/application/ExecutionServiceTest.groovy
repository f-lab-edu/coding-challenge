package code.application

import code.domain.*
import code.dto.*
import code.exception.ExecutionException
import code.exception.NotFoundException
import code.execution.ExecutionService
import code.executor.CodeExecutorService
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

import java.time.LocalDateTime

class ExecutionServiceTest extends Specification {

    def static final JAVA_CODE = """
                    public class Main {
                        public static void main(String[] args) {
                            System.out.println("Hello World!");
                        }
                    }
                """
    private static final String JAVA11 = "JAVA11"
    private static final String MEMBER_ID = "this-is-user-id"
    private static final String QUESTION_ID = UUID.randomUUID().toString()
    private static final UserCode CODE = new UserCode(Lang.JAVA11, JAVA_CODE)
    private static final TestCases TEST_CASES = new TestCases(Arrays.asList(new TestCase("", "Hello World!")))
    private static final ExecutionRequest REQUEST = new ExecutionRequest(JAVA_CODE, JAVA11)

    private final executorService = Mock(CodeExecutorService)
    private final memberRepository = Mock(MemberRepository)
    private final questionRepository = Mock(QuestionRepository)
    private final executionResultRepository = Mock(ExecutionResultRepository)
    private final executionService = new ExecutionService(executorService, memberRepository, questionRepository, executionResultRepository)

    //C14
    def "코드를 실행하고 성공한 실행 결과를 반환받는다"() {
        given:
        def succeededResult = new SucceededResult(QUESTION_ID, MEMBER_ID, CODE, true, LocalDateTime.now(),
                1000L, 234L)
        def succeededTestResult = new SucceededTestResult(true, 1000L, 234L)

        when:
        memberRepository.findById(MEMBER_ID) >> Mono.just(new Member(MEMBER_ID))
        questionRepository.findById(QUESTION_ID) >> Mono.just(new Question(QUESTION_ID, TEST_CASES))
        executorService.executeCode(_, _) >> Mono.just(succeededTestResult)
        executionResultRepository.save(_) >> Mono.just(succeededResult)

        then:
        StepVerifier.create(executionService.executeCode(MEMBER_ID, QUESTION_ID, REQUEST))
                .consumeNextWith({ executionResult ->
                    assert executionResult.isSucceeded()
                }).verifyComplete()
    }

    //C14
    def "코드를 실행하고 실패한 실행 결과를 반환받는다"() {
        given:
        def succeededResult = new FailedResult(QUESTION_ID, MEMBER_ID, CODE, false, LocalDateTime.now(),
                Cause.WRONG_ANSWER, "결과 값이 `Hello World`가 아닙니다., 출력된 결과는 `Hello World!`입니다.")
        def succeededTestResult = new FailedTestResult(false, Cause.WRONG_ANSWER,
                "결과 값이 `Hello World`가 아닙니다., 출력된 결과는 `Hello World!`입니다.")

        when:
        memberRepository.findById(MEMBER_ID) >> Mono.just(new Member(MEMBER_ID))
        questionRepository.findById(QUESTION_ID) >> Mono.just(new Question(QUESTION_ID, TEST_CASES))
        executorService.executeCode(_, _) >> Mono.just(succeededTestResult)
        executionResultRepository.save(_) >> Mono.just(succeededResult)

        then:
        StepVerifier.create(executionService.executeCode(MEMBER_ID, QUESTION_ID, REQUEST))
                .consumeNextWith({ executionResult ->
                    assert !executionResult.isSucceeded()
                }).verifyComplete()
    }

    //C15
    def "코드를 실행할 때, 사용자 정보가 없으면 `NotFoundException`예외가 발생한다"() {
        when:
        memberRepository.findById(MEMBER_ID) >> Mono.empty()

        then:
        StepVerifier.create(executionService.executeCode(MEMBER_ID, QUESTION_ID, REQUEST))
                .verifyError(NotFoundException.class)
    }

    //C16
    def "코드를 실행할 때, 문제 정보가 없으면 `NotFoundException`예외가 발생한다"() {
        when:
        memberRepository.findById(MEMBER_ID) >> Mono.just(new Member(MEMBER_ID))
        questionRepository.findById(QUESTION_ID) >> Mono.empty()

        then:
        StepVerifier.create(executionService.executeCode(MEMBER_ID, QUESTION_ID, REQUEST))
                .verifyError(NotFoundException.class)
    }

    //C51
    def "코드를 실행할 때, `executionCode`에서 반환받은 데이터가 없다면 `NotFoundException` 예외가 발생한다"() {
        when:
        memberRepository.findById(MEMBER_ID) >> Mono.just(new Member(MEMBER_ID))
        questionRepository.findById(QUESTION_ID) >> Mono.just(new Question(QUESTION_ID, TEST_CASES))
        executorService.executeCode(_, _) >> Mono.empty()

        then:
        StepVerifier.create(executionService.executeCode(MEMBER_ID, QUESTION_ID, REQUEST))
                .verifyError(ExecutionException.class)
    }

    //C17
    def "결과 리스트를 조회한다"() {
        when:
        memberRepository.findById(MEMBER_ID) >> Mono.just(new Member(MEMBER_ID))
        questionRepository.findById(QUESTION_ID) >> Mono.just(new Question(QUESTION_ID, TEST_CASES))
        executionResultRepository.findAllByMemberIdAndQuestionId(MEMBER_ID, QUESTION_ID) >> Flux.just(
                new SucceededResult(QUESTION_ID, MEMBER_ID, CODE, true, LocalDateTime.now(),
                        1000L, 234L),
                new FailedResult(QUESTION_ID, MEMBER_ID, CODE, false, LocalDateTime.now(),
                        Cause.WRONG_ANSWER, "결과 값이 `Hello World`가 아닙니다. 출력된 결과는 `Hello World!`입니다."))
        then:
        StepVerifier.create(executionService.findResults(MEMBER_ID, QUESTION_ID))
                .expectNextCount(2)
                .verifyComplete()
    }

    //C18
    def "결과 리스트를 조회할 때 사용자 정보를 식별할 수 없다면 `NotFoundUserException` 예외가 발생한다"() {
        when:
        memberRepository.findById(MEMBER_ID) >> Mono.empty()

        then:
        StepVerifier.create(executionService.findResults(MEMBER_ID, QUESTION_ID))
                .verifyError(NotFoundException.class)
    }

    //C19
    def "결과 리스트를 조회할 때 문제 정보(`Question`)가 존재하지 않으면 `NotFountQuestionException` 예외가 발생한다"() {
        when:
        memberRepository.findById(MEMBER_ID) >> Mono.just(new Member(MEMBER_ID))
        questionRepository.findById(QUESTION_ID) >> Mono.empty()

        then:
        StepVerifier.create(executionService.findResults(MEMBER_ID, QUESTION_ID))
                .verifyError(NotFoundException.class)
    }


    //C20
    def "결과 리스트를 조회할 때, 값이 비어있으면 빈 리스트를 반환한다"() {
        when:
        memberRepository.findById(MEMBER_ID) >> Mono.just(new Member(MEMBER_ID))
        questionRepository.findById(QUESTION_ID) >> Mono.just(new Question(QUESTION_ID,
                new TestCases(Arrays.asList(new TestCase("", "Hello World!")))))
        executionResultRepository.findAllByMemberIdAndQuestionId(MEMBER_ID, QUESTION_ID) >> Flux.empty()

        then:
        StepVerifier.create(executionService.findResults(MEMBER_ID, QUESTION_ID))
                .expectNextCount(0)
                .verifyComplete()
    }

    //C21
    def "결과를 조회한다"() {
        given:
        def resultId = UUID.randomUUID().toString()

        when:
        memberRepository.findById(MEMBER_ID) >> Mono.just(new Member(MEMBER_ID))
        executionResultRepository.findById(resultId) >> Mono.just(new SucceededResult(QUESTION_ID, MEMBER_ID, CODE,
                true, LocalDateTime.now(), 1000L, 234L))

        then:
        StepVerifier.create(executionService.findResult(MEMBER_ID, resultId))
                .consumeNextWith({ executionResult ->
                    assert executionResult.isSucceeded()
                    assert executionResult.questionId() == QUESTION_ID
                }).verifyComplete()
    }

    //C22
    def "결과를 조회할 때 사용자 정보를 식별할 수 없다면 `NotFoundUserException` 예외가 발생한다"() {
        given:
        def resultId = UUID.randomUUID().toString()

        when:
        memberRepository.findById(MEMBER_ID) >> Mono.empty()

        then:
        StepVerifier.create(executionService.findResult(MEMBER_ID, resultId))
                .verifyError(NotFoundException.class)
    }

    //C23
    def "결과를 조회할 때, 결과 식별자에 맞는 결과가 없다면 `NotFoundResultException` 예외가 발생한다"() {
        given:
        def resultId = UUID.randomUUID().toString()

        when:
        memberRepository.findById(MEMBER_ID) >> Mono.just(new Member(MEMBER_ID))
        executionResultRepository.findById(resultId) >> Mono.empty()

        then:
        StepVerifier.create(executionService.findResult(MEMBER_ID, resultId))
                .verifyError(NotFoundException.class)
    }
}
