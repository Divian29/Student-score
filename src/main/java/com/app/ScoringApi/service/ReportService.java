package com.app.ScoringApi.service;

import com.app.ScoringApi.dto.StudentReportDTO;
import com.app.ScoringApi.entities.Score;
import com.app.ScoringApi.entities.Student;
import com.app.ScoringApi.repository.ScoreRepository;
import com.app.ScoringApi.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final StudentRepository studentRepo;
    private final ScoreRepository scoreRepo;

    public StudentReportDTO buildReportForStudent(Student s) {
        List<Score> scores = scoreRepo.findByStudentId(s.getId());
        List<Integer> values = scores.stream()
                .map(Score::getScore).sorted().collect(Collectors.toList());

        Map<String,Integer> map = scores.stream()
                .collect(Collectors.toMap(sc -> sc.getSubject().name(), Score::getScore));

        double mean = calculateMean(values);
        double median = calcMedian(values);
        List<Integer> modes = calcMode(values);

        return new StudentReportDTO(s.getId(), s.getName(), map, round(mean), round(median), modes);
    }


    private double calculateMean(List<Integer> scores) {
        return scores.stream().mapToInt(i -> i).average().orElse(0);
    }


    private double calcMedian(List<Integer> sorted) {
        if(sorted.isEmpty()) return 0;
        int n = sorted.size();
        if(n % 2 == 1) return sorted.get(n/2);
        return (sorted.get(n/2 -1) + sorted.get(n/2)) / 2.0;
    }

    private List<Integer> calcMode(List<Integer> values) {
        if(values.isEmpty()) return Collections.emptyList();
        Map<Integer, Long> freq = values.stream()
                .collect(Collectors.groupingBy(v->v, Collectors.counting()));
        long max = freq.values().stream().mapToLong(Long::longValue).max().orElse(1);
        return freq.entrySet().stream()
                .filter(e->e.getValue()==max)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }

    private double round(double v){
        return Math.round(v * 100.0) / 100.0;
    }


    // Report endpoint with pagination and filtering by name and minMean (example)
    public Page<StudentReportDTO> getReport(Pageable pageable, String nameFilter, Double minMean, Double maxMean) {
        Page<Student> studentsPage;
        if(nameFilter != null && !nameFilter.isBlank()) {
            // simple search by name: using ExampleMatcher or custom repo; using in-memory filter for demo
            List<Student> all = studentRepo.findAll();
            List<Student> filtered = all.stream()
                    .filter(s -> s.getName().toLowerCase().contains(nameFilter.toLowerCase()))
                    .collect(Collectors.toList());
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), filtered.size());
            List<Student> sub = start <= end ? filtered.subList(start, end) : Collections.emptyList();
            studentsPage = new PageImpl<>(sub, pageable, filtered.size());
        } else {
            studentsPage = studentRepo.findAll(pageable);
        }


        List<StudentReportDTO> list = studentsPage.stream()
                .map(this::buildReportForStudent)
                .filter(dto -> {
                    if(minMean != null && dto.mean() < minMean) return false;
                    if(maxMean != null && dto.mean() > maxMean) return false;
                    return true;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(list, pageable, studentsPage.getTotalElements());
    }



}
