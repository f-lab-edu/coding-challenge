package code.ui.v1

import code.dto.ExecutionRequest
import code.dto.ExecutionResponse
import code.execution.ExecutionService
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

import java.time.LocalDateTime

@ExtendWith(MockitoExtension.class)
class ExecutionControllerTest extends Specification {

    public static final String USER_ID = "this-is-user-id"
    def executionService = Mock(ExecutionService)
    def executionController = new ExecutionController(executionService)

    //C7
    def "코드를 실행한다."() {
        given:
        def questionId = getId()
        def resultId = getId()
        def isSucceeded = true
        def executionRequest = new ExecutionRequest("""
            public class Main {
                public static void main(String[] args) {
                    System.out.println("hello");
                }
            }
        """, "java11")

        when:
        1 * executionService.executeCode(USER_ID, questionId, executionRequest) >> Mono.just(new ExecutionResponse(resultId,
                questionId, isSucceeded, "성공", LocalDateTime.now()))

        then:
        StepVerifier.create(executionController.executeCode(questionId, executionRequest)).consumeNextWith({ entityModel ->
            entityModel.content.resultId() == resultId
            entityModel.content.questionId() == questionId
            entityModel.content.isSucceeded() == isSucceeded
        }).verifyComplete()
    }

    //C9
    def "결과 리스트를 조회한다."() {
        given:
        def questionId = "123D"

        when:
        1 * executionService.findResults(USER_ID, questionId) >> Flux.just(
                new ExecutionResponse(getId(), questionId, true, "성공", LocalDateTime.now()),
                new ExecutionResponse(getId(), questionId, true, "성공", LocalDateTime.now()),
                new ExecutionResponse(getId(), questionId, true, "성공", LocalDateTime.now())
        )

        then:
        StepVerifier.create(executionController.findResults(questionId))
                .consumeNextWith({ collectionModel ->
                    collectionModel.content.size() == 3
                }).verifyComplete()
    }

    //C11
    def "결과를 조회한다."() {
        given:
        def resultId = getId()

        when:
        1 * executionService.findResult(USER_ID, resultId) >> Mono.just(new ExecutionResponse(resultId,
                getId(), true, "성공", LocalDateTime.now()))

        then:
        StepVerifier.create(executionController.findResult(resultId)).consumeNextWith({ entityModel ->
            entityModel.content.resultId() == resultId
        }).verifyComplete()
    }

    private String getId() {
        UUID.randomUUID().toString()
    }
}
