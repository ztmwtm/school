package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public boolean deleteStudent(long id) {
         return studentRepository.deleteStudentById(id);
    }

    public Collection<Student> getStudentsByAge(int age) {
        return studentRepository.findStudentsByAge(age);
    }

    public Collection<Student> getStudentsByAgeBetween(int startAge, int endAge) {
        return studentRepository.findStudentsByAgeBetween(startAge, endAge);
    }

    public Integer getTotalAmount() {
        return studentRepository.getTotalAmount();
    }

    public Double getAverageAge() {
        return studentRepository.getAverageAge();
    }

    public List<Student> getLastStudents(int count) {
        return studentRepository.getLastStudents(count);
    }
}
