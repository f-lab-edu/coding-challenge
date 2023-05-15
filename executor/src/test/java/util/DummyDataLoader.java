package util;

import java.util.List;
import java.util.Map;

import code.domain.Member;
import code.domain.MemberRepository;
import code.domain.Question;
import code.domain.QuestionRepository;
import code.dto.TestCase;
import code.dto.TestCases;

public class DummyDataLoader {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    public DummyDataLoader(QuestionRepository questionRepository, MemberRepository memberRepository) {
        this.questionRepository = questionRepository;
        this.memberRepository = memberRepository;
    }

    public Map<String, String> loadData() {
        final var question_key = "question test key";
        questionRepository
                .save(new Question(question_key, new TestCases(List.of(new TestCase("", "Hello World!")))))
                .subscribe();

        final var memberKey = "this-is-user-id";
        memberRepository.save(new Member(memberKey));

        return Map.of("questionId", question_key);
    }
}
