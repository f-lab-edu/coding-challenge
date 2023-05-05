package code.infra;

import org.springframework.stereotype.Repository;

import code.domain.Question;
import code.domain.QuestionRepository;
import reactor.core.publisher.Mono;

@Repository
public class MemoryQuestionRepository implements QuestionRepository {
    @Override
    public Mono<Question> findById(String questionId) {
        return null;
    }
}
