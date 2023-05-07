package docs.executor;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;

import com.fasterxml.jackson.core.JsonProcessingException;

import code.dto.ExecutionResponse;
import code.exception.NotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@DisplayName("[ExecutorDocumentation] : execution controller 문서화")
final class ExecutionControllerDoc extends ExecutorDocumentation {

    private static final String QUESTION_ID = createId();
    private static final ExecutionResponse SUCCEEDED_EXECUTION_RESPONSE = getSucceededExecutionResponse(
            createId());

    private static final ExecutionResponse DIFFERENT_EXECUTION_RESPONSE = new ExecutionResponse(
            createId(), QUESTION_ID, false,
            """
                    테스트가 실패했습니다. 출력 값은 "Hello World!"이지만 결과 값은 "Hello, world" 여야 합니다.
                    """, LocalDateTime.of(2021, 8, 1, 12, 23, 0));

    private static final ExecutionResponse ERROR_EXECUTION_RESPONSE = new ExecutionResponse(
            createId(), QUESTION_ID, false,
            """
                    테스트가 실패했습니다. 발생한 예외는 "IllegalArgumentException" 입니다.
                    """, LocalDateTime.of(2023, 3, 2, 11, 23, 1));

    //C4
    @Test
    void post__executeCode_returnSucceededResult() throws JsonProcessingException {
        when(executionService.executeCode(any(), any(), any()))
                .thenReturn(Mono.just(SUCCEEDED_EXECUTION_RESPONSE));
        post__executeCode();
    }

    //C4
    @Test
    void post__executeCode_returnFailedResult_CauseIsDifferent() throws JsonProcessingException {
        when(executionService.executeCode(any(), any(), any()))
                .thenReturn(Mono.just(DIFFERENT_EXECUTION_RESPONSE));
        post__executeCode();
    }

    //C4
    @Test
    void post__executeCode_returnFailedResult_CauseIsError() throws JsonProcessingException {
        when(executionService.executeCode(any(), any(), any()))
                .thenReturn(Mono.just(ERROR_EXECUTION_RESPONSE));
        post__executeCode();
    }

    //C4
    @Test
    void post__executeCode_inputNullData() throws JsonProcessingException {
        var contents = new HashMap<String, String>();
        contents.put("lang", null);
        contents.put("code", null);
        var contentsString = objectMapper.writeValueAsString(contents);

        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/codes/executions")
                        .queryParam("questionId", QUESTION_ID)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contentsString)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .consumeWith(
                        document("executor/{method-name}",
                                 getDocumentRequest(),
                                 getDocumentResponse()
                        ));
    }


    //C5
    @Test
    void get_findResults() {
        when(executionService.findResults(any(), any()))
                .thenReturn(Flux.just(DIFFERENT_EXECUTION_RESPONSE, SUCCEEDED_EXECUTION_RESPONSE));

        webTestClient.get().uri(uriBuilder -> uriBuilder
                             .path("/v1/codes/results")
                             .queryParam("questionId", QUESTION_ID)
                             .build())
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody()
                     .consumeWith(
                             document("executor/{method-name}",
                                      getDocumentRequest(),
                                      getDocumentResponse(),
                                      queryParameters(parameterWithName("questionId").description(
                                              "문제 식별자를 의미하며 Null 일 수 없습니다.")
                                      )
                             )
                     );
    }

    //C5
    @Test
    void get_findResults_returnEmptyCollection() {
        when(executionService.findResults(any(), any()))
                .thenReturn(Flux.empty());

        webTestClient.get().uri(uriBuilder -> uriBuilder
                             .path("/v1/codes/results")
                             .queryParam("questionId", QUESTION_ID)
                             .build())
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody()
                     .consumeWith(
                             document("executor/{method-name}",
                                      getDocumentRequest(),
                                      getDocumentResponse(),
                                      queryParameters(parameterWithName("questionId").description(
                                              "문제 식별자를 의미하며 Null 일 수 없습니다.")
                                      )
                             )
                     );
    }

    //C6
    @Test
    void get__findResult_returnSucceededResult() {
        String resultId = createId();
        when(executionService.findResult(any(), any()))
                .thenReturn(Mono.just(getSucceededExecutionResponse(resultId)));

        webTestClient.get().uri("/v1/codes/results/{resultsId}", resultId)
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody()
                     .consumeWith(
                             document("executor/{method-name}",
                                      getDocumentRequest(),
                                      getDocumentResponse(),
                                      pathParameters(parameterWithName("resultsId").description(
                                              "결과 식별자를 의미하며 Null 일 수 없습니다.")
                                      )
                             )
                     );
    }

    //C6
    @Test
    void get__findResult_notFound() {
        String resultId = createId();
        when(executionService.findResult(any(), any()))
                .thenReturn(Mono.error(NotFoundException.notFoundResult(resultId)));

        webTestClient.get().uri("/v1/codes/results/{resultsId}", resultId)
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus().isNotFound()
                     .expectBody()
                     .consumeWith(
                             document("executor/{method-name}",
                                      getDocumentRequest(),
                                      getDocumentResponse(),
                                      pathParameters(parameterWithName("resultsId").description(
                                              "결과 식별자를 의미하며 Null 일 수 없습니다.")
                                      )
                             )
                     );
    }

    private static String createId() {
        return UUID.randomUUID().toString();
    }

    private static ExecutionResponse getSucceededExecutionResponse(String resultId) {
        return new ExecutionResponse(
                resultId, QUESTION_ID, true, """
                테스트가 성공했습니다. 평균 실행 시간은 100ms 이며 평균 메모리 사용량은 300KB 입니다.
                """, LocalDateTime.of(2021, 8, 1, 7, 31, 30));
    }

    private void post__executeCode() throws JsonProcessingException {
        var contents = new HashMap<String, String>();
        contents.put("lang", "JAVA11");
        contents.put("code", """
                public class Main {
                    public static void main(String[] args) {
                        System.out.println("Hello, world!");
                    }
                }
                """);

        var contentsString = objectMapper.writeValueAsString(contents);
        var requestFieldDescriptors = new FieldDescriptor[] {
                fieldWithPath("lang").description("사용자가 작성한 코드의 타입. `JAVA11`과 `PYTHON3` 중 하나의 정보를 가진다."),
                fieldWithPath("code").description("사용자가 작성한 코드. 비어있을 수 없다.")
        };

        var parameterDescriptor = parameterWithName("questionId")
                .description("문제 식별자를 의미하며 Null 일 수 없습니다.");

        webTestClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/codes/executions")
                        .queryParam("questionId", QUESTION_ID)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(contentsString)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(
                        document("executor/{method-name}",
                                 getDocumentRequest(),
                                 getDocumentResponse(),
                                 queryParameters(parameterDescriptor),
                                 requestFields(requestFieldDescriptors)
                        ));
    }
}
