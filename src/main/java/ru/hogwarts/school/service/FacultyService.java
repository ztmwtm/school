package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (facultyRepository.findById(faculty.getId()).isPresent()) {
            return facultyRepository.save(faculty);
        }
        return null;
    }

    public boolean deleteFaculty(long id) {
        return facultyRepository.deleteFacultyById(id);
    }

    public Optional<Faculty> getFacultyByColor(String color) {
        return facultyRepository.findFacultyByColorIgnoreCase(color);
    }

    public Optional<Faculty> getFacultyByName(String name) {
        return facultyRepository.findFacultyByNameIgnoreCase(name);
    }

}
