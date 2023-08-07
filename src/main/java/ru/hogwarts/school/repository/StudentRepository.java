package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;


public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findStudentsByAge(int age);

    Collection<Student> findStudentsByAgeBetween(int startAge, int endAge);

    boolean deleteStudentById(Long id);

    @Query(value = "SELECT count(*) FROM student", nativeQuery = true)
    Integer getTotalAmount();

    @Query(value = "SELECT avg(age) FROM student", nativeQuery = true)
    Double getAverageAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT ?1", nativeQuery = true)
    List<Student> getLastStudents(int count);
}
