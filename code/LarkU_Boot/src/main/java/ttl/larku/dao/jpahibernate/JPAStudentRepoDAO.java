package ttl.larku.dao.jpahibernate;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ttl.larku.dao.BaseDAO;
import ttl.larku.dao.repository.StudentRepo;
import ttl.larku.domain.Student;

@Repository
@Transactional
@Profile("production")
public class JPAStudentRepoDAO implements BaseDAO<Student> {

    @Resource
    private StudentRepo repo;

    public JPAStudentRepoDAO() {
        int i = 0;
    }

    @Override
    public void update(Student updateObject) {
        repo.saveAndFlush(updateObject);
    }

    @Override
    public void delete(Student objToDelete) {
        Optional<Student> optional = repo.findById(objToDelete.getId());
        optional.ifPresent(managed -> {
            repo.delete(managed);
        });
    }

    @Override
    public Student create(Student newObject) {
        newObject = repo.saveAndFlush(newObject);
        return newObject;
    }

    @Override
    public Student get(int id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public List<Student> getAll() {
        List<Student> students = repo.findAll();
        return students;
    }

    @Override
    public void deleteStore() {
        repo.deleteAll();
    }

    @Override
    public void createStore() {
    }
}