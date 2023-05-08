package code.execution;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import code.domain.ExecutionResult;
import code.domain.ExecutionResultRepository;
import code.domain.ExecutorService;
import code.domain.FailedResult;
import code.domain.Lang;
import code.domain.MemberRepository;
import code.domain.Question;
import code.domain.QuestionRepository;
import code.domain.SucceededResult;
import code.domain.UserCode;
import code.dto.ExecutionRequest;
import code.dto.ExecutionResponse;
import code.dto.FailedTestResult;
import code.dto.SucceededTestResult;
import code.dto.TestResult;
import code.exception.ExecutionException;
import code.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExecutionService {
    private static final String FAILED_MESSAGE_FORMAT = "테스트가 실패했습니다. %s";
    private static final String SUCCEEDED_MESSAGE_FORMAT =
            "테스트가 성공했습니다. 평균 실행 시간은 %sms 이며, 평균 메모리 사용량은 %sKB 입니다.";
    private final ExecutorService executorService;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final ExecutionResultRepository executionResultRepository;

    public Mono<ExecutionResponse> executeCode(String memberId, String questionId, ExecutionRequest request) {
        return memberRepository.findById(memberId)
                               .switchIfEmpty(Mono.error(NotFoundException.notFoundMember(memberId)))
                               .flatMap(user -> questionRepository.findById(questionId))
                               .switchIfEmpty(Mono.error(NotFoundException.notFoundQuestion(questionId)))
                               .flatMap(question -> executeCode(request, question))
                               .switchIfEmpty(Mono.error(ExecutionException.executeFailed()))
                               .flatMap(testResult -> getResultMono(memberId, questionId, request, testResult))
                               .flatMap(executionResult -> Mono.just(getExecutionResponse(executionResult)));
    }

    public Mono<ExecutionResponse> findResult(String memberId, String resultId) {
        return memberRepository.findById(memberId)
                               .switchIfEmpty(Mono.error(NotFoundException.notFoundMember(memberId)))
                               .flatMap(user -> executionResultRepository.findById(resultId))
                               .switchIfEmpty(Mono.error(NotFoundException.notFoundResult(resultId)))
                               .flatMap(result -> Mono.just(getExecutionResponse(result)));
    }

    public Flux<ExecutionResponse> findResults(String memberId, String questionId) {
        return memberRepository.findById(memberId)
                               .switchIfEmpty(Mono.error(NotFoundException.notFoundMember(memberId)))
                               .flatMap(user -> questionRepository.findById(questionId))
                               .switchIfEmpty(Mono.error(NotFoundException.notFoundQuestion(questionId)))
                               .flux()
                               .flatMap(question -> executionResultRepository
                                       .findAllByMemberIdAndQuestionId(memberId, questionId))
                               .flatMap(result -> Flux.just(getExecutionResponse(result)));
    }

    private static ExecutionResponse getExecutionResponse(ExecutionResult result) {
        if (result instanceof SucceededResult succeededResult) {
            return getSucceededResultResponse(succeededResult);
        }
        return getFailedResultResponse((FailedResult) result);
    }

    private static ExecutionResponse getFailedResultResponse(FailedResult failedResult) {
        return new ExecutionResponse(failedResult.getId(), failedResult.getQuestionId(),
                                     failedResult.getIsSucceed(),
                                     String.format(FAILED_MESSAGE_FORMAT, failedResult.getMessage()),
                                     failedResult.getCreatedAt());
    }

    private static ExecutionResponse getSucceededResultResponse(SucceededResult succeededResult) {
        return new ExecutionResponse(succeededResult.getId(), succeededResult.getQuestionId(),
                                     succeededResult.getIsSucceed(),
                                     String.format(
                                             SUCCEEDED_MESSAGE_FORMAT,
                                             succeededResult.getTotalExecutionTime(),
                                             succeededResult.getAverageMemoryUsage()),
                                     succeededResult.getCreatedAt());
    }

    private Mono<ExecutionResult> getResultMono(String memberId, String questionId,
                                                ExecutionRequest request, TestResult testResult) {
        if (testResult.isSucceeded()) {
            return executionResultRepository.save(
                    getSucceededResult(memberId, questionId, request, (SucceededTestResult) testResult));
        }
        return executionResultRepository.save(
                getFailedResult(memberId, questionId, request, (FailedTestResult) testResult));
    }

    private static FailedResult getFailedResult(String memberId, String questionId, ExecutionRequest request,
                                                FailedTestResult testResult) {
        return new FailedResult(questionId, memberId,
                                new UserCode(Lang.valueOf(request.lang()), request.code()),
                                false, LocalDateTime.now(), testResult.getCause(), testResult.getMessage());
    }

    private static SucceededResult getSucceededResult(String memberId, String questionId,
                                                      ExecutionRequest request,
                                                      SucceededTestResult testResult) {
        return new SucceededResult(questionId, memberId,
                                   new UserCode(Lang.valueOf(request.lang()), request.code()),
                                   true, LocalDateTime.now(), testResult.getTotalExecutionTime(),
                                   testResult.getAverageMemoryUsage()
        );
    }

    private Mono<TestResult> executeCode(ExecutionRequest request, Question question) {
        return executorService.executeCode(new UserCode(Lang.JAVA11, request.code()), question.getTestCases());
    }
}
