package ru.hogwarts.school.controller;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty != null) {
            return ResponseEntity.ok(faculty);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("{id}/students")
    public ResponseEntity<Collection<Student>> getStudentFaculty(@PathVariable Long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty != null) {
            return ResponseEntity.ok(faculty.getStudents());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping()
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping()
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty editedFaculty = facultyService.editFaculty(faculty);
        if (editedFaculty != null) {
            return ResponseEntity.ok(editedFaculty);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<Object> deleteFaculty(@PathVariable Long id) {
        return facultyService.deleteFaculty(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<Faculty> getFacultyByNameOrColor(@RequestParam(required = false, value = "color") @Nullable String color,
                                                     @RequestParam(required = false, value = "name") @Nullable String name) {
        Optional<Faculty> result = Optional.empty();
        if (Objects.nonNull(name)) {
            result = facultyService.getFacultyByName(name);
        }
        if (result.isEmpty() && Objects.nonNull(color)) {
            result = facultyService.getFacultyByColor(color);
        }
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
