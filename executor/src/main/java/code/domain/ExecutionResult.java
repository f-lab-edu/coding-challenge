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
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
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
}
