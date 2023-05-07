package code.domain;

import java.time.LocalDateTime;

import code.dto.Code;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ExecutionResult {
    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;
    @ToString.Include
    private final String questionId;
    @ToString.Include
    private final String userId;
    @ToString.Include
    private final Code code;
    @ToString.Include
    private final Boolean isSucceed;
    @ToString.Include
    private final LocalDateTime createdAt;

    public ExecutionResult(String id, String questionId, String userId, Code code, Boolean isSucceed,
                           LocalDateTime createdAt) {
        if (code == null) {
            throw new IllegalArgumentException("code must not be null");
        }

        if (questionId == null) {
            throw new IllegalArgumentException("questionId must not be null");
        }

        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }

        this.id = id;
        this.questionId = questionId;
        this.userId = userId;
        this.code = code;
        this.isSucceed = isSucceed;
        this.createdAt = createdAt;
    }

    ExecutionResult(String questionId, String userId, Code code, Boolean isSucceed,
                    LocalDateTime createdAt) {
        this(null, questionId, userId, code, isSucceed, createdAt);
    }
}
