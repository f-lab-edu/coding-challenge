package code.application;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import code.domain.ExecutionResult;
import code.domain.ExecutionResultRepository;
import code.domain.ExecutorService;
import code.domain.FailedResult;
import code.domain.MemberRepository;
import code.domain.Question;
import code.domain.QuestionRepository;
import code.domain.SucceededResult;
import code.dto.Code;
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
                               .flatMap(testResult -> saveResult(memberId, questionId,
                                                                 request, testResult))
                               .flatMap(result -> {
                                   if (result instanceof SucceededResult succeededResult) {
                                       return Mono.just(
                                               getSucceededResultResponse(succeededResult));
                                   }
                                   return Mono.just(
                                           getFailedResultResponse((FailedResult) result));
                               });
    }

    public Mono<ExecutionResponse> findResult(String memberId, String resultId) {
        return null;
    }

    public Flux<ExecutionResponse> findResults(String memberId, String questionId) {
        return null;
    }

    private static ExecutionResponse getFailedResultResponse(FailedResult failedResult) {
        return new ExecutionResponse(failedResult.getId(), failedResult.getQuestionId(),
                                     failedResult.getIsSucceed(),
                                     String.format("테스트가 실패했습니다. %s", failedResult.getMessage()),
                                     failedResult.getCreatedAt());
    }

    private static ExecutionResponse getSucceededResultResponse(SucceededResult succeededResult) {
        return new ExecutionResponse(succeededResult.getId(), succeededResult.getQuestionId(),
                                     succeededResult.getIsSucceed(),
                                     String.format(
                                             "테스트가 성공했습니다. 평균 실행 시간은 %sms 이며, 평균 메모리 사용량은 %sKB 입니다.",
                                             succeededResult.getTotalExecutionTime(),
                                             succeededResult.getAverageMemoryUsage()),
                                     succeededResult.getCreatedAt());
    }

    private Mono<ExecutionResult> saveResult(String memberId, String questionId,
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
        return new FailedResult(questionId, memberId, request.code(), request.lang(), false,
                                LocalDateTime.now(), testResult.getCause(), testResult.getMessage());
    }

    private static SucceededResult getSucceededResult(String memberId, String questionId,
                                                      ExecutionRequest request,
                                                      SucceededTestResult testResult) {
        return new SucceededResult(questionId, memberId, request.code(), request.lang(), true,
                                   LocalDateTime.now(), testResult.getTotalExecutionTime(),
                                   testResult.getAverageMemoryUsage()
        );
    }

    private Mono<TestResult> executeCode(ExecutionRequest request, Question question) {
        return executorService.executeCode(new Code(request.lang(), request.code()), question.getTestCases());
    }
}
