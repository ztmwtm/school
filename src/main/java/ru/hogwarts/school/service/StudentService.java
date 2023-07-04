package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class StudentService {

    private Map<Long, Student> students = new HashMap<>();

    private long lastID = 0L;

    public Student createStudent(Student student) {
        student.setId(++lastID);
        students.put(lastID, student);
        return student;
    }

    public Student findStudent(long id) {
        return students.get(id);
    }

    public Student editStudent(Student student) {
        if (Objects.nonNull(student) && students.containsKey(student.getId())) {
            students.put(student.getId(), student);
            return student;
        }
        return null;
    }

    public Student deleteStudent(long id) {
        return students.remove(id);
    }

    public Collection<Student> getStudentsByAge(int age) {
        return students.values().stream().filter(student -> student.getAge() == age).toList();
    }
}
