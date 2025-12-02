package com.app.ScoringApi;

import com.app.ScoringApi.repository.ScoreRepository;
import com.app.ScoringApi.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@ActiveProfiles("test")
public class StudentIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private ScoreRepository scoreRepo;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        scoreRepo.deleteAll();
        studentRepo.deleteAll();
    }

    @Test
    void createStudent_returnsStudentWithId() throws Exception {
        String json = """
            {
              "name": "Frank",
              "scores": [
                {"subject": "MATHS","score": 100},
                {"subject": "ENGLISH","score": 97},
                {"subject": "BIOLOGY","score": 80},
                {"subject": "CHEMISTRY","score": 100},
                {"subject": "PHYSICS","score": 100}
              ]
            }
            """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        assertEquals(1, studentRepo.count());
        assertEquals(5, scoreRepo.count());
    }

}
