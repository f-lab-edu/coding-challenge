package code.dto;

import java.time.LocalDateTime;

public record ExecutionResponse(
        String resultId,
        String questionId,
        boolean isSucceeded,
        String message,
        LocalDateTime SubmittedTime
) {}
