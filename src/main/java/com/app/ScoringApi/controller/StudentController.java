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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/report")
    @Operation(summary = "Get paginated student report (scores, mean, median, mode)")
    public ResponseEntity<?> report(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minMean,
            @RequestParam(required = false) Double maxMean
    ) {
        String[] sortParts = sort.split(",");
        Sort s = Sort.by(Sort.Direction.fromString(sortParts[1]), sortParts[0]);
        Pageable pageable = PageRequest.of(page, size, s);
        Page<?> result = reportService.getReport(pageable, name, minMean, maxMean);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        return studentRepo.findById(id)
                .map(reportService::buildReportForStudent)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
