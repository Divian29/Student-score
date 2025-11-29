package com.app.ScoringApi.dto;

import com.app.ScoringApi.enums.Subject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ScoreRequest(@NotNull Subject subject,
                           @NotNull @Min(0) @Max(100) Integer score) {
}
