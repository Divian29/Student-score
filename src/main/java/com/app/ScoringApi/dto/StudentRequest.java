package com.app.ScoringApi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record StudentRequest(@NotBlank String name,
                             @NotEmpty List<ScoreRequest> scores) {
}
