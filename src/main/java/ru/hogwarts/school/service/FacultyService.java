package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class FacultyService {

    private Map<Long, Faculty> faculties = new HashMap<>();

    private long counter = 0L;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++counter);
        faculties.put(counter, faculty);
        return faculty;
    }

    public Faculty findFaculty(long id) {
        return faculties.get(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (Objects.nonNull(faculty) && faculties.containsKey(faculty.getId())) {
            faculties.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }

    public Faculty deleteFaculty(long id) {
        return faculties.remove(id);
    }

    public Collection<Faculty> getFacultyByColor(String color) {
        return faculties.values().stream().filter(faculty -> faculty.getColor().equalsIgnoreCase(color)).toList();
    }
}
