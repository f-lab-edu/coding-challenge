package code.dto;

import java.time.LocalDateTime;
import java.util.Date;

public record ExecutionResponse(
        String resultId,
        String questionId,
        boolean isSucceeded,
        String message,
        LocalDateTime SubmittedTime
) {}
