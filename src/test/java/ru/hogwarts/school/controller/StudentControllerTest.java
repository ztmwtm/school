package ru.hogwarts.school.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Autowired
    private ObjectMapper objectMapper;

    static final long studentOneId = 1L;
    static final long studentTwoId = 2L;
    static final long invalidId = 999L;
    static final String studentOneName = "studentOne";
    static final String studentTwoName = "studentTwo";
    static final int studentOneAge = 16;
    static final int studentTwoAge = 17;
    static final String editedName = "studentOne-eee";
    static final int editedAge = 18;
    static final int invalidAge = 999;
    static final long facultyId = 1L;

    static final String facultyColor = "red";

    static final String facultyName = "gryffindor";


    Student studentOne;
    Student studentTwo;

    Faculty faculty;

    JSONObject jsonObjectOne;
    JSONObject jsonObjectTwo;

    @BeforeEach
    void init() throws JSONException {
        faculty = new Faculty();
        faculty.setName(facultyName);
        faculty.setColor(facultyColor);
        faculty.setId(facultyId);

        studentOne = new Student();
        studentOne.setId(studentOneId);
        studentOne.setName(studentOneName);
        studentOne.setAge(studentOneAge);
        studentOne.setFaculty(faculty);

        studentTwo = new Student();
        studentTwo.setId(studentTwoId);
        studentTwo.setName(studentTwoName);
        studentTwo.setAge(studentTwoAge);
        studentTwo.setFaculty(faculty);

        jsonObjectOne = new JSONObject();
        jsonObjectOne.put("id", studentOneId);
        jsonObjectOne.put("name", studentOneName);
        jsonObjectOne.put("age", studentOneAge);

        jsonObjectTwo = new JSONObject();
        jsonObjectTwo.put("id", studentTwoId);
        jsonObjectTwo.put("name", studentTwoName);
        jsonObjectTwo.put("age", studentTwoAge);

    }

    @Test
    void getStudentInfo() throws Exception {
        when(studentRepository.findById(studentOneId)).thenReturn(Optional.of(studentOne));
        when(studentRepository.findById(studentTwoId)).thenReturn(Optional.of(studentTwo));
        when(studentRepository.findById(invalidId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + studentOneId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Student studentResult = objectMapper.readValue(
                            result.getResponse().getContentAsString(), Student.class
                    );
                    assertThat(studentResult).isNotNull();
                    assertThat(studentResult).isEqualTo(studentOne);
                });

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentFaculty() throws Exception {
        when(studentRepository.findById(studentOneId)).thenReturn(Optional.of(studentOne));
        when(studentRepository.findById(studentTwoId)).thenReturn(Optional.of(studentTwo));
        when(studentRepository.findById(invalidId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + studentOneId + "/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Faculty facultyResult = objectMapper.readValue(
                            result.getResponse().getContentAsString(), Faculty.class
                    );
                    assertThat(facultyResult).isNotNull();
                    assertThat(facultyResult).isEqualTo(faculty);
                });

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + invalidId + "/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound());
    }

    @Test
    void createStudent() throws Exception {
        when(studentRepository.save(any(Student.class))).thenReturn(studentOne);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(jsonObjectOne.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Student studentResult = objectMapper.readValue(
                            result.getResponse().getContentAsString(), Student.class
                    );
                    assertThat(studentResult).isNotNull();
                    assertThat(studentResult).isEqualTo(studentOne);
                });
    }

    @Test
    void editStudent() throws Exception {
        when(studentRepository.findById(studentOneId)).thenReturn(Optional.of(studentOne));
        when(studentRepository.save(any(Student.class))).thenReturn(studentOne);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObjectOne.toString()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Student studentResult = objectMapper.readValue(
                            result.getResponse().getContentAsString(), Student.class
                    );
                    assertThat(studentResult).isNotNull();
                    assertThat(studentResult).isEqualTo(studentOne);
                });

        Student studentEdited = new Student();
        studentEdited.setId(studentOneId);
        studentEdited.setName(editedName);
        studentEdited.setAge(editedAge);


        JSONObject jsonObjectEdited = new JSONObject();
        jsonObjectEdited.put("id", studentOneId);
        jsonObjectEdited.put("name", editedName);
        jsonObjectEdited.put("age", editedAge);

        when(studentRepository.findById(studentOneId)).thenReturn(Optional.of(studentEdited));
        when(studentRepository.save(any(Student.class))).thenReturn(studentEdited);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObjectEdited.toString()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Student studentResult = objectMapper.readValue(
                            result.getResponse().getContentAsString(), Student.class
                    );
                    assertThat(studentResult).isNotNull();
                    assertThat(studentResult).isEqualTo(studentEdited);
                });
    }

    @Test
    void deleteStudent() throws Exception {
        when(studentRepository.findById(studentOneId)).thenReturn(Optional.of(studentOne));
        when(studentRepository.deleteStudentById(studentOneId)).thenReturn(true);
        when(studentRepository.deleteStudentById(invalidId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + studentOneId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound());
    }

    @Test
    void getStudentsByAge() throws Exception {
        when(studentRepository.findStudentsByAgeBetween(studentOneAge, studentTwoAge)).thenReturn(List.of(studentOne, studentTwo));
        when(studentRepository.findStudentsByAge(studentOneAge)).thenReturn(List.of(studentOne));
        when(studentRepository.findStudentsByAge(studentTwoAge)).thenReturn(List.of(studentTwo));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?age=" + studentOneAge)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<Student> students = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertThat(students).isNotNull();
                    assertThat(students.get(0)).isEqualTo(studentOne);
                });

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?startAge=" + studentOneAge + "&endAge=" + studentTwoAge)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<Student> students = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertThat(students).isNotNull();
                    assertThat(students.get(0)).isEqualTo(studentOne);
                    assertThat(students.get(1)).isEqualTo(studentTwo);
                });

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?age=" + invalidAge)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<Student> students = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertThat(students).isNotNull();
                    assertThat(students).isEmpty();
                });

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student?startAge=" + studentTwoAge + "&endAge=" + studentOneAge)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<Student> students = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertThat(students).isNotNull();
                    assertThat(students).isEmpty();
                });


    }
}