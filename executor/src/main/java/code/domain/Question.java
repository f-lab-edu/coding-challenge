package code.domain;

import code.dto.TestCases;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public final class Question {
    @ToString.Include
    @EqualsAndHashCode.Include
    private final String id;
    @ToString.Include
    private final TestCases testCases;
}
