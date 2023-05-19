package code.domain;

import reactor.core.publisher.Mono;

public interface QuestionRepository {
    Mono<Question> findById(String questionId);

    Mono<Question> save(Question question);
}
