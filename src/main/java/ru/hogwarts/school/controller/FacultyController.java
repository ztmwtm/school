package ru.hogwarts.school.controller;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    Logger logger = LoggerFactory.getLogger(FacultyController.class);

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        logger.info("Was invoked method for get faculty");
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty != null) {
            return ResponseEntity.ok(faculty);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("{id}/students")
    public ResponseEntity<Collection<Student>> getStudentFaculty(@PathVariable Long id) {
        logger.info("Was invoked method for get students by faculty");
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty != null) {
            return ResponseEntity.ok(faculty.getStudents());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping()
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyService.createFaculty(faculty);
    }

    @PutMapping()
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        Faculty editedFaculty = facultyService.editFaculty(faculty);
        if (editedFaculty != null) {
            return ResponseEntity.ok(editedFaculty);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<Object> deleteFaculty(@PathVariable Long id) {
        logger.info("Was invoked method for delete faculty");
        return facultyService.deleteFaculty(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping()
    public ResponseEntity<Faculty> getFacultyByNameOrColor(@RequestParam(required = false, value = "color") @Nullable String color,
                                                           @RequestParam(required = false, value = "name") @Nullable String name) {
        logger.info("Was invoked method for get faculty by name or color");
        Optional<Faculty> result = Optional.empty();
        if (Objects.nonNull(name)) {
            result = facultyService.getFacultyByName(name);
        }
        if (result.isEmpty() && Objects.nonNull(color)) {
            result = facultyService.getFacultyByColor(color);
        }
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/longestName")
    public ResponseEntity<String> getLongestFacultyName() {
        logger.info("Was invoked method for get longest faculty name");
        return ResponseEntity.ok(
                facultyService.getAllFaculties().stream()
                        .map(Faculty::getName)
                        .reduce((s1, s2) -> s1.length() > s2.length() ? s1 : s2)
                        .orElse(null));
    }

    @GetMapping("/stream")
    public ResponseEntity<Integer> streamTest() {
        logger.info("Was invoked method for stream test");
                long nanoTime = System.nanoTime();
                Integer result = Stream.iterate(1, a -> a + 1)
                        .parallel()
                        .limit(1_000_000)
                        .reduce(0, Integer::sum);
                nanoTime = System.nanoTime() - nanoTime;
                logger.info("Completed in {} milliseconds", nanoTime / 1_000_000);
        return ResponseEntity.ok(result);
    }
}
