package com.app.ScoringApi;

import com.app.ScoringApi.dto.StudentReportDTO;
import com.app.ScoringApi.entities.Score;
import com.app.ScoringApi.entities.Student;
import com.app.ScoringApi.enums.Subject;
import com.app.ScoringApi.repository.ScoreRepository;
import com.app.ScoringApi.service.ReportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ReportServiceUnitTest {
    @Mock
    private ScoreRepository scoreRepo;

    @InjectMocks
    private ReportService reportService;

    @Test
    void testBuildReportForStudent_calculatesMeanMedianMode() {
        Student student = new Student();
        student.setId(1L);
        student.setName("Frank");

        List<Score> scores = List.of(
                new Score(student, Subject.MATHS, 100),
                new Score(student, Subject.ENGLISH, 97),
                new Score(student, Subject.BIOLOGY, 80),
                new Score(student, Subject.CHEMISTRY, 100),
                new Score(student, Subject.PHYSICS, 100)
        );

        when(scoreRepo.findByStudentId(1L)).thenReturn(scores);

        StudentReportDTO report = reportService.buildReportForStudent(student);

        assertEquals(95.4, report.mean());
        assertEquals(100, report.median());
        assertEquals(List.of(100), report.mode());
    }
}
