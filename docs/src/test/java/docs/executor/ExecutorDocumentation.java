package docs.executor;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import code.ExecutorApplication;
import code.execution.ExecutionService;
import code.ui.v1.ExecutionController;
import docs.TestTemplate;

@AutoConfigureRestDocs
@AutoConfigureWebTestClient
@WebFluxTest(controllers = ExecutionController.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ExecutorApplication.class)
class ExecutorDocumentation extends TestTemplate {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected WebTestClient webTestClient;

    @MockBean
    protected ExecutionService executionService;

    protected OperationRequestPreprocessor getDocumentRequest() {
        return Preprocessors.preprocessRequest(
                Preprocessors.modifyUris()
                             .scheme("http")
                             .host("127.0.0.1")
                             .port(8082),
                Preprocessors.prettyPrint()
        );
    }

    protected OperationResponsePreprocessor getDocumentResponse() {
        return Preprocessors.preprocessResponse(Preprocessors.prettyPrint());
    }
}
