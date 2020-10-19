package ttl.larku.dao.inmemory;

import ttl.larku.domain.Student;

import java.util.List;

public interface BaseDAO {
    void update(Student updateObject);

    void delete(Student student);

    Student create(Student newObject);

    Student get(int id);

    List<Student> getAll();
}
