package code.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
public final class FailedResult extends ExecutionResult {

    @ToString.Include
    private final Cause cause;
    @ToString.Include
    private final String message;

    public FailedResult(String questionId, String userId, String code, String lang, Boolean isSucceed,
                        LocalDateTime createdAt, Cause cause, String message) {
        super(questionId, userId, code, lang, isSucceed, createdAt);
        this.cause = cause;
        this.message = message;
    }
}
