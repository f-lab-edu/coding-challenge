package util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import code.domain.Cause;
import code.domain.FailedResult;
import code.domain.Lang;
import code.domain.Member;
import code.domain.Question;
import code.domain.SucceededResult;
import code.domain.UserCode;
import code.dto.TestCase;
import code.dto.TestCases;
import code.infra.MemoryExecutionResultRepository;
import code.infra.MemoryMemberRepository;
import code.infra.MemoryQuestionRepository;

public class MemoryDummyDataLoader implements DummyDataLoader {
    private static final UserCode WRONG_CODE = new UserCode(Lang.JAVA11, """
            public class Main {
                public static void main(String[] args) {
                    System.out.println("Hello World");
                }
            }
            """);
    private static final UserCode JAVA_CODE = new UserCode(Lang.JAVA11, """
            public class Main {
                public static void main(String[] args) {
                    System.out.println("Hello World!");
                }
            }
            """);
    private final MemoryQuestionRepository questionRepository;
    private final MemoryMemberRepository memberRepository;
    private final MemoryExecutionResultRepository resultRepository;

    public MemoryDummyDataLoader(MemoryQuestionRepository questionRepository,
                                 MemoryMemberRepository memberRepository,
                                 MemoryExecutionResultRepository resultRepository) {
        this.questionRepository = questionRepository;
        this.memberRepository = memberRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public Map<String, String> loadData() {
        final var question_key = "question test key";
        final var memberKey = "this-is-user-id";
        final var succeededResultKey = "succeeded result key";

        questionRepository
                .save(new Question(question_key, new TestCases(List.of(new TestCase("", "Hello World!")))))
                .subscribe();
        memberRepository.save(new Member(memberKey)).subscribe();
        resultRepository.save(new FailedResult(null, question_key, memberKey, WRONG_CODE,
                                               false, LocalDateTime.now(), Cause.WRONG_ANSWER, "wrong answer"))
                        .subscribe();
        resultRepository.save(new SucceededResult(succeededResultKey, question_key, memberKey, JAVA_CODE,
                                                  true, LocalDateTime.now(), 31L, 1235L))
                        .subscribe();

        return Map.of("questionId", question_key, "memberId", memberKey, "resultId", succeededResultKey);
    }

    @Override
    public void cleanUp() {
        resultRepository.deleteAll().subscribe();
        questionRepository.deleteAll().subscribe();
        memberRepository.deleteAll().subscribe();
    }
}
