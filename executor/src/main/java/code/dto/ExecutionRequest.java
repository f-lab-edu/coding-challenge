package code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExecutionRequest(
        @NotNull
        @NotBlank
        String code,
        @NotNull
        @NotBlank
        String lang
) {
}
