package ru.hogwarts.school.controller;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/student")
public class StudentController {

    Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        logger.info("Was invoked method for get student");
        Student student = studentService.findStudent(id);
        if (student != null) {
            return ResponseEntity.ok(student);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/amount")
    public ResponseEntity<Integer> getStudentAmount() {
        logger.info("Was invoked method for get students amount");
        return ResponseEntity.ok(studentService.getTotalAmount());
    }

    @GetMapping("/age/average")
    public ResponseEntity<Double> getStudentAverageAge() {
        logger.info("Was invoked method for get students average age");
        return ResponseEntity.ok(
                studentService.getAllStudents().stream()
                        .mapToInt(Student::getAge)
                        .average()
                        .orElse(0)
        );
    }

    @GetMapping("/last/{count}")
    public ResponseEntity<List<Student>> getLastStudents(@PathVariable int count) {
        logger.info("Was invoked method for get last students by id");
        return ResponseEntity.ok(studentService.getLastStudents(count));
    }


    @GetMapping("{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        logger.info("Was invoked method for get faculty of student");
        Student student = studentService.findStudent(id);
        if (student != null) {
            return ResponseEntity.ok(student.getFaculty());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping()
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        logger.info("Was invoked method for create student");
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    @PutMapping()
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        logger.info("Was invoked method for edit student");
        Student editedStudent = studentService.editStudent(student);
        if (editedStudent != null) {
            return ResponseEntity.ok(editedStudent);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<Object> deleteStudent(@PathVariable Long id) {
        logger.info("Was invoked method for delete student");
        return studentService.deleteStudent(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<Collection<Student>> getStudentsByAge(@RequestParam(required = false, value = "age") @Nullable Integer age,
                                                                @RequestParam(required = false, value = "startAge") @Nullable Integer startAge,
                                                                @RequestParam(required = false, value = "endAge") @Nullable Integer endAge) {
        logger.info("Was invoked method for get students by age");
        if (Objects.nonNull(age) && age > 0) {
            return ResponseEntity.ok(studentService.getStudentsByAge(age));
        }

        if (Objects.nonNull(startAge) && Objects.nonNull(endAge)
                && startAge > 0 && endAge > 0 && startAge <= endAge) {
            return ResponseEntity.ok(studentService.getStudentsByAgeBetween(startAge, endAge));
        }

        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/names/{char}")
    public ResponseEntity<List<String>> getStudentsNamesSorted(@PathVariable("char") String ch) {
        logger.info("Was invoked method for get students by name");
        return ResponseEntity.ok(
                studentService.getAllStudents().stream()
                        .map(Student::getName)
                        .map(String::toUpperCase)
                        .filter(s -> s.startsWith(ch.toUpperCase()))
                        .sorted()
                        .toList());
    }
}
