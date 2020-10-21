package ttl.larku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ttl.larku.dao.BaseDAO;
import ttl.larku.dao.repository.StudentRepo;
import ttl.larku.domain.Student;

import java.util.List;

@Service
public class StudentRepoService {

    @Autowired
    private StudentRepo studentDAO;

    public StudentRepoService() {
    }

    private CourseService cs;

    public Student createStudent(String name, String phoneNumber, Student.Status status) {
        Student student = new Student(name, phoneNumber, status);
        return createStudent(student);

//        student = studentDAO.save(student);
//
//        return student;
    }

    public Student createStudent(Student student) {
        student = studentDAO.save(student);

        return student;
    }

    public void deleteStudent(int id) {
        Student student = studentDAO.findById(id).orElse(null);
        if (student != null) {
            studentDAO.delete(student);
        }
    }

    public void updateStudent(Student student) {
        studentDAO.save(student);
    }

    public Student getStudent(int id) {
        return studentDAO.findById(id).orElse(null);
    }

    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

//    public BaseDAO<Student> getStudentDAO() {
//        return studentDAO;
//    }
//
//    public void setStudentDAO(BaseDAO<Student> studentDAO) {
//        this.studentDAO = studentDAO;
//    }
//
//    public void clear() {
//        studentDAO.deleteStore();
//        studentDAO.createStore();
//    }
//
//    public CourseService getCs() {
//        return cs;
//    }
//
//    public void setCs(CourseService cs) {
//        this.cs = cs;
//    }
}
