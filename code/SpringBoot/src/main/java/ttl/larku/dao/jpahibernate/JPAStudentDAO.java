package ttl.larku.dao.jpahibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Student;

public class JPAStudentDAO implements BaseDAO<Student> {

    private Map<Integer, Student> students = new HashMap<Integer, Student>();
    private static int nextId = 0;

    private String from;

    public JPAStudentDAO(String from) {
        this.from = from + ": ";
    }

    public JPAStudentDAO() {
        this("JPA");
    }

    public void update(Student updateObject) {
        if (students.containsKey(updateObject.getId())) {
            students.put(updateObject.getId(), updateObject);
        }
    }

    public void delete(Student student) {
        students.remove(student.getId());
    }

    public Student create(Student newObject) {
        //Create a new Id
        int newId = nextId++;
        newObject.setId(newId);

        //Put our Mark
        newObject.setName(from + newObject.getName());
        students.put(newId, newObject);

        return newObject;
    }

    public Student get(int id) {
        return students.get(id);
    }

    public List<Student> getAll() {
        return new ArrayList<Student>(students.values());
    }

    public void deleteStore() {
        students = null;
    }

    public void createStore() {
        students = new HashMap<Integer, Student>();
    }

    public Map<Integer, Student> getStudents() {
        return students;
    }
}
