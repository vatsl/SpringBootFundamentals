package ttl.larku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Student;
import ttl.larku.domain.Student.Status;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentDaoService implements StudentService {

    @Autowired
    private BaseDAO<Student> studentDAO;

    @Override
    public Student createStudent(String name) {
        Student student = new Student(name);
        student = studentDAO.create(student);

        return student;
    }

    @Override
    public Student createStudent(String name, String phoneNumber, Status status) {
        return createStudent(new Student(name, phoneNumber, status));
    }

    @Override
    public Student createStudent(Student student) {
        student = studentDAO.create(student);

        return student;
    }

    @Override
    public void deleteStudent(int id) {
        Student student = studentDAO.get(id);
        if (student != null) {
            studentDAO.delete(student);
        }
    }

    @Override
    public void updateStudent(Student student) {
        studentDAO.update(student);
    }

    @Override
    public Student getStudent(int id) {
        return studentDAO.get(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.getAll();
    }

    @Override
    public List<Student> getByName(String name) {
        String lc = name.toLowerCase();
        List<Student> result = getAllStudents()
                .stream()
                .filter(s -> s.getName().toLowerCase().contains(lc))
                .collect(Collectors.toList());
        return result;
    }

    public BaseDAO<Student> getStudentDAO() {
        return studentDAO;
    }

    public void setStudentDAO(BaseDAO<Student> studentDAO) {
        this.studentDAO = studentDAO;
    }

    @Override
    public void clear() {
        studentDAO.deleteStore();
        studentDAO.createStore();
    }

}
