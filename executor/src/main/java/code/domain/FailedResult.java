package code.domain;

import java.time.LocalDateTime;

import code.dto.Code;
import code.exception.InvalidFailedResultException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
public final class FailedResult extends ExecutionResult {

    @ToString.Include
    private final Cause cause;
    @ToString.Include
    private final String message;

    public FailedResult(String questionId, String userId, Code code, Boolean isSucceed,
                        LocalDateTime createdAt, Cause cause, String message) {
        super(questionId, userId, code, isSucceed, createdAt);

        if (cause == null) {
            throw InvalidFailedResultException.invalidCause();
        }

        if (message == null || message.isBlank()) {
            throw InvalidFailedResultException.invalidMessage();
        }

        this.cause = cause;
        this.message = message;
    }
}
