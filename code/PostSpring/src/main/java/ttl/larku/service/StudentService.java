package ttl.larku.service;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Student;

import java.util.List;

public class StudentService {

    private BaseDAO<Student> studentDAO;

    public StudentService() {
    }

    private CourseService cs;

    public Student createStudent(String name, String phoneNumber, Student.Status status) {
        Student student = new Student(name, phoneNumber, status);
        student = studentDAO.create(student);

        return student;
    }

    public Student createStudent(Student student) {
        student = studentDAO.create(student);

        return student;
    }

    public void deleteStudent(int id) {
        Student student = studentDAO.get(id);
        if (student != null) {
            studentDAO.delete(student);
        }
    }

    public void updateStudent(Student student) {
        studentDAO.update(student);
    }

    public Student getStudent(int id) {
        return studentDAO.get(id);
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAll();
    }

    public BaseDAO<Student> getStudentDAO() {
        return studentDAO;
    }

    public void setStudentDAO(BaseDAO<Student> studentDAO) {
        this.studentDAO = studentDAO;
    }

    public void clear() {
        studentDAO.deleteStore();
        studentDAO.createStore();
    }

    public CourseService getCs() {
        return cs;
    }

    public void setCs(CourseService cs) {
        this.cs = cs;
    }
}
