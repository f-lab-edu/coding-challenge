package code.domain;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ExecutionResult {
    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;
    @ToString.Include
    private final String questionId;
    @ToString.Include
    private final String userId;
    private final String code;
    private final String lang;
    @ToString.Include
    private final Boolean isSucceed;
    @ToString.Include
    private final LocalDateTime createdAt;
}
