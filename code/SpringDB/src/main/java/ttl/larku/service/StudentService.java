package ttl.larku.service;

import org.springframework.transaction.annotation.Transactional;
import ttl.larku.domain.Student;

import java.util.List;

@Transactional
public interface StudentService {
    Student createStudent(String name);

    Student createStudent(String name, String phoneNumber, Student.Status status);

    Student createStudent(Student student);

    void deleteStudent(int id);

    void updateStudent(Student student);

    Student getStudent(int id);

    List<Student> getAllStudents();

    List<Student> getByName(String name);

    void clear();
}
