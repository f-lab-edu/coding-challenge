package code.integration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import code.dto.ExecutionResponse;
import code.infra.MemoryExecutionResultRepository;
import code.infra.MemoryMemberRepository;
import code.infra.MemoryQuestionRepository;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import util.DummyDataLoader;
import util.MemoryDummyDataLoader;

@SpringBootTest
@AutoConfigureWebTestClient
public class ExecutorIntegrationTest {
    private static final String JAVA_11 = "JAVA11";
    private static final String JAVA_CODE = """
            public class Main {
                public static void main(String[] args) {
                    System.out.println("Hello World!");
                }
            }
            """;
    private static final String INCORRECT_JAVA_CODE = """
            public class Main {
                public static void main(String[] args) {
                    System.out.println("INCORRECT MESSAGE");
                }
            }
            """;
    private static final String INVALID_JAVA_CODE = """
            public class Main {
                public static void main(String[] args) {
                    System.out.println(0/0);
                }
            }
            """;
    private static final String PYTHON_3 = "PYTHON3";
    private static final String PYTHON_CODE = """
            print("Hello World!")
            """;
    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    private MemoryQuestionRepository questionRepository;
    @Autowired
    private MemoryMemberRepository memberRepository;
    @Autowired
    private MemoryExecutionResultRepository resultRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private Map<String, String> loadedData;

    @BeforeEach
    void setUp() {
        webTestClient = webTestClient.mutate()
                                     .responseTimeout(Duration.ofSeconds(20))
                                     .build();
        DummyDataLoader dataLoader = new MemoryDummyDataLoader(questionRepository, memberRepository,
                                                               resultRepository);
        dataLoader.cleanUp();
        loadedData = dataLoader.loadData();
    }

    // C34
    @Test
    @DisplayName("자바 코드를 실행해 성공한 결과를 반환받는다.")
    void executeJavaCode() throws JsonProcessingException {
        // given
        var questionId = getQuestionId();

        // when
        var response = executeCode(questionId, JAVA_11, JAVA_CODE);

        // then
        StepVerifier.create(response)
                    .consumeNextWith(result -> {
                        var executionResponse = getExecutionResponse(result);
                        assert executionResponse.isSucceeded();
                        assert executionResponse.resultId() != null;
                    })
                    .verifyComplete();
    }

    // C34
    @Test
    @DisplayName("파이썬 코드를 실행해 성공한 결과를 반환받는다.")
    void executePythonCode() throws JsonProcessingException {
        // given
        var questionId = getQuestionId();

        // when
        var response = executeCode(questionId, PYTHON_3, PYTHON_CODE);

        // then
        StepVerifier.create(response)
                    .consumeNextWith(result -> {
                        var executionResponse = getExecutionResponse(result);
                        assert executionResponse.isSucceeded();
                        assert executionResponse.resultId() != null;
                    })
                    .verifyComplete();
    }

    // C55
    @Test
    @DisplayName("결과가 잘못된 코드를 실행하면 실패한 결과를 반환받는다.")
    void executeIncorrectCode() throws JsonProcessingException {
        // given
        var questionId = getQuestionId();
        // when
        var response = executeCode(questionId, JAVA_11, INCORRECT_JAVA_CODE);

        // then
        StepVerifier.create(response)
                    .consumeNextWith(result -> {
                        var executionResponse = getExecutionResponse(result);
                        assert !executionResponse.isSucceeded();
                        assert executionResponse.resultId() != null;
                    })
                    .verifyComplete();
    }

    // C54
    @Test
    @DisplayName("오류가 발생한 코드를 실행하면 오류 결과를 반환받는다.")
    void executeInvalidCode() throws JsonProcessingException {
        // given
        var questionId = getQuestionId();

        // when
        var response = executeCode(questionId, JAVA_11, INVALID_JAVA_CODE);

        // then
        StepVerifier.create(response)
                    .consumeNextWith(result -> {
                        var executionResponse = getExecutionResponse(result);
                        assert !executionResponse.isSucceeded();
                        assert executionResponse.resultId() != null;
                    })
                    .verifyComplete();
    }

    // C52
    @Test
    @DisplayName("사용자 식별자와 문제 식별자를 입력해 결과 리스트를 조회한다.")
    void findResults() {
        // given
        var questionId = getQuestionId();
        var memberId = getMemberId();
        var results = resultRepository.findAllByMemberIdAndQuestionId(memberId, questionId)
                                      .collectList()
                                      .block();

        // when & then
        StepVerifier.create(findResults(questionId))
                    .consumeNextWith(result -> {
                        assert result.getContent().size() == results.size();
                    })
                    .verifyComplete();
    }

    // C53
    @Test
    @DisplayName("결과 식별자를 입력해 결과를 조회한다.")
    void findResult() {
        // given
        var resultId = getResultId();
        var result = resultRepository.findById(resultId).block();

        // when & then
        StepVerifier.create(findResult(resultId))
                    .consumeNextWith(response -> {
                        var executionResponse = getExecutionResponse(response);
                        assert executionResponse.isSucceeded() == result.getIsSucceed();
                    }).verifyComplete();
    }

    private Flux<EntityModel> executeCode(String questionId, String lang, String code)
            throws JsonProcessingException {
        var contents = new HashMap<String, String>();
        contents.put("lang", lang);
        contents.put("code", code);

        var contentsString = objectMapper.writeValueAsString(contents);

        return webTestClient.post()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/v1/codes/executions")
                                    .queryParam("questionId", questionId)
                                    .build())
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(contentsString)
                            .accept(MediaType.TEXT_EVENT_STREAM)
                            .exchange()
                            .expectStatus().isOk()
                            .returnResult(EntityModel.class)
                            .getResponseBody();
    }

    private Flux<CollectionModel> findResults(String questionId) {
        return webTestClient.get().uri(uriBuilder -> uriBuilder
                                    .path("/v1/codes/results")
                                    .queryParam("questionId", questionId)
                                    .build())
                            .accept(MediaType.APPLICATION_JSON)
                            .exchange()
                            .expectStatus().isOk()
                            .returnResult(CollectionModel.class).getResponseBody();
    }

    private Flux<EntityModel> findResult(String resultId) {
        return webTestClient.get().uri("/v1/codes/results/{resultsId}", resultId)
                            .accept(MediaType.APPLICATION_JSON)
                            .exchange()
                            .expectStatus().isOk()
                            .returnResult(EntityModel.class).getResponseBody();
    }

    private String getQuestionId() {
        return loadedData.get("questionId");
    }

    private String getMemberId() {
        return loadedData.get("memberId");
    }

    private String getResultId() {
        return loadedData.get("resultId");
    }

    private ExecutionResponse getExecutionResponse(EntityModel result) {
        return objectMapper.convertValue(result, ExecutionResponse.class);
    }
}
