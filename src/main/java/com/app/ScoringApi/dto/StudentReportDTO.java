package com.app.ScoringApi.dto;

import java.util.List;
import java.util.Map;

public record StudentReportDTO(
        Long studentId,
        String name,
        Map<String,Integer> scores,
        Double mean,
        Double median,
        List<Integer> mode
) {
}
