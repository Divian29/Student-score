package com.app.ScoringApi.controller;

import com.app.ScoringApi.dto.StudentRequest;
import com.app.ScoringApi.entities.Score;
import com.app.ScoringApi.entities.Student;
import com.app.ScoringApi.repository.ScoreRepository;
import com.app.ScoringApi.repository.StudentRepository;
import com.app.ScoringApi.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentController {
    private final StudentRepository studentRepo;
    private final ScoreRepository scoreRepo;
    private final ReportService reportService;


    @PostMapping("/students")
    @Operation(summary = "Create a student with their 5 subject scores")
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentRequest request) {
        Student s = new Student();
        s.setName(request.name());
        Student savedStudent = studentRepo.save(s);

        // save scores
        request.scores().forEach(sr -> {
            Score score = new Score();
            score.setSubject(sr.subject());
            score.setScore(sr.score());
            score.setStudent(savedStudent);
            scoreRepo.save(score);
        });

        return ResponseEntity.ok(Map.of("id", s.getId(), "name", s.getName()));
    }

}
