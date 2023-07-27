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
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FacultyController.class)
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Autowired
    private ObjectMapper objectMapper;


    static final Long id = 1L;
    static final String name = "gryffindor";
    static final String invalidName = "gryff";
    static final String color = "red";
    static final String invalidColor = "Redddd";

    static final Long invalidId = 2L;

    static final String editedName = "gryffindor_is_the_BEST";
    static final String editedColor = "reded";

    static final Long studentOneId = 1L;
    static final Long studentTwoId = 2L;
    static final String studentOneName = "studentOne";
    static final String studentTwoName = "studentTwo";
    static final int studentOneAge = 16;
    static final int studentTwoAge = 17;

    private Faculty faculty;

    private JSONObject jsonObject;

    @BeforeEach
    void init() throws JSONException {
        faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);


        jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("name", name);
        jsonObject.put("color", color);
    }

    @Test
    void getFacultyInfo() throws Exception {

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        when(facultyRepository.findById(invalidId)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Faculty facultyResult = objectMapper.readValue(
                            result.getResponse().getContentAsString(), Faculty.class);
                    assertThat(facultyResult).isNotNull();
                    assertThat(facultyResult).isEqualTo(faculty);
                });

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isNotFound());


    }

    @Test
    void getStudentFaculty() throws Exception {
        Student student1 = new Student();
        student1.setId(studentOneId);
        student1.setName(studentOneName);
        student1.setAge(studentOneAge);
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(studentTwoId);
        student2.setName(studentTwoName);
        student2.setAge(studentTwoAge);
        student2.setFaculty(faculty);

        faculty.setStudents(List.of(student1, student2));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("", faculty.getStudents());

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        when(facultyRepository.findById(invalidId)).thenReturn(Optional.empty());


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id + "/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<Student> students = objectMapper.readValue(
                            result.getResponse().getContentAsString(), new TypeReference<>() {
                            });
                    assertThat(students).isNotNull();
                    assertThat(students.get(0)).isEqualTo(student1);
                    assertThat(students.get(1)).isEqualTo(student2);
                });


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + invalidId + "/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void createFaculty() throws Exception {
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Faculty facultyResult = objectMapper.readValue(
                            result.getResponse().getContentAsString(), Faculty.class);
                    assertThat(facultyResult).isNotNull();
                    assertThat(facultyResult).isEqualTo(faculty);
                });
    }

    @Test
    void editFaculty() throws Exception {
        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Faculty facultyResult = objectMapper.readValue(
                            result.getResponse().getContentAsString(), Faculty.class);
                    assertThat(facultyResult).isNotNull();
                    assertThat(facultyResult).isEqualTo(faculty);
                });

        Faculty facultyEdited = new Faculty();
        facultyEdited.setId(id);
        facultyEdited.setName(editedName);
        facultyEdited.setColor(editedColor);


        JSONObject jsonObjectEdited = new JSONObject();
        jsonObjectEdited.put("id", id);
        jsonObjectEdited.put("name", editedName);
        jsonObjectEdited.put("color", editedColor);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(facultyEdited);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObjectEdited.toString()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Faculty facultyResult = objectMapper.readValue(
                            result.getResponse().getContentAsString(), Faculty.class);
                    assertThat(facultyResult).isNotNull();
                    assertThat(facultyResult).isEqualTo(facultyEdited);
                });
    }

    @Test
    void deleteFaculty() throws Exception {
        when(facultyRepository.findById(id)).thenReturn(Optional.of(faculty));
        when(facultyRepository.deleteFacultyById(id)).thenReturn(true);
        when(facultyRepository.deleteFacultyById(invalidId)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getFacultyByNameOrColor() throws Exception {
        when(facultyRepository.findFacultyByNameIgnoreCase(name)).thenReturn(Optional.of(faculty));
        when(facultyRepository.findFacultyByColorIgnoreCase(color)).thenReturn(Optional.of(faculty));
        when(facultyRepository.findFacultyByNameIgnoreCase(invalidName)).thenReturn(Optional.empty());
        when(facultyRepository.findFacultyByColorIgnoreCase(invalidColor)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color=" + color)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Faculty facultyResult = objectMapper.readValue(
                            result.getResponse().getContentAsString(), Faculty.class);
                    assertThat(facultyResult).isNotNull();
                    assertThat(facultyResult).isEqualTo(faculty);
                });

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?name=" + name)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Faculty facultyResult = objectMapper.readValue(
                            result.getResponse().getContentAsString(), Faculty.class);
                    assertThat(facultyResult).isNotNull();
                    assertThat(facultyResult).isEqualTo(faculty);
                });

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color=" + invalidColor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?name=" + invalidName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isNotFound());
    }
}