package code.ui.v1;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import code.execution.ExecutionService;
import code.dto.ExecutionRequest;
import code.dto.ExecutionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/codes")
@RequiredArgsConstructor
public class ExecutionController {

    private final ExecutionService executionService;

    @PostMapping(
            value = "/executions",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public Mono<EntityModel<ExecutionResponse>> executeCode(@RequestParam String questionId,
                                                            @Valid @RequestBody ExecutionRequest request) {
        return executionService.executeCode(getUserId(), questionId, request).flatMap(
                executionResponse -> {
                    var controller = methodOn(ExecutionController.class);
                    return Mono.zip(
                            Mono.just(executionResponse),
                            linkTo(controller.findResult(executionResponse.resultId()))
                                    .withRel("result").toMono(),
                            linkTo(controller.findResults(executionResponse.questionId()))
                                    .withRel("results").toMono()
                    );
                }
        ).map(o -> EntityModel.of(o.getT1(), Links.of(o.getT2(), o.getT3())));
    }

    @GetMapping(
            value = "/results/{resultId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<EntityModel<ExecutionResponse>> findResult(@PathVariable String resultId) {
        return executionService.findResult(getUserId(), resultId).flatMap(
                executionResponse -> {
                    var controller = methodOn(ExecutionController.class);
                    return Mono.zip(
                            Mono.just(executionResponse),
                            linkTo(controller.findResult(executionResponse.resultId()))
                                    .withRel("result").toMono(),
                            linkTo(controller.findResults(executionResponse.questionId()))
                                    .withRel("results").toMono()
                    );
                }
        ).map(o -> EntityModel.of(o.getT1(), Links.of(o.getT2(), o.getT3())));
    }

    @GetMapping(
            value = "/results",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<CollectionModel<EntityModel<ExecutionResponse>>> findResults(@RequestParam String questionId) {
        return executionService.findResults(getUserId(), questionId)
                               .flatMap(executionResponse -> {
                                   var controller = methodOn(ExecutionController.class);
                                   return Mono.zip(
                                           Mono.just(executionResponse),
                                           linkTo(controller.findResult(executionResponse.resultId()))
                                                   .withSelfRel().toMono()
                                   );
                               }).map(o -> EntityModel.of(o.getT1(), o.getT2()))
                               .collectList()
                               .flatMap(executionResponses -> {
                                   var controller = methodOn(ExecutionController.class);
                                   return Mono.zip(
                                           Mono.just(executionResponses),
                                           linkTo(controller.findResults(questionId))
                                                   .withSelfRel().toMono()
                                   );
                               }).map(o -> CollectionModel.of(o.getT1(), o.getT2()));
    }

    private String getUserId() {
        return "this-is-user-id";
    }
}
