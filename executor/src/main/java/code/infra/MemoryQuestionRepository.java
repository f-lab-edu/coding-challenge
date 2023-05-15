package code.infra;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import code.domain.Question;
import code.domain.QuestionRepository;
import reactor.core.publisher.Mono;

@Repository
public class MemoryQuestionRepository implements QuestionRepository {
    private final Map<String, Question> map = new HashMap<>();

    @Override
    public Mono<Question> findById(String questionId) {
        return Mono.just(map.get(questionId));
    }

    @Override
    public Mono<Question> save(Question question) {
        adjustMaximumCapacity();
        if (question.getId() != null) {
            map.put(question.getId(), question);
            return Mono.just(question);
        }
        Question newQuestion = new Question(UUID.randomUUID().toString(), question.getTestCases());
        map.put(newQuestion.getId(), newQuestion);
        return Mono.just(question);
    }

    public Mono<Void> deleteAll() {
        return Mono.fromRunnable(map::clear);
    }

    private void adjustMaximumCapacity() {
        if (map.size() > 200) {
            String id = map.values().stream()
                           .min(Comparator.comparing(Question::getId))
                           .get().getId();
            map.remove(id);
        }
    }
}
