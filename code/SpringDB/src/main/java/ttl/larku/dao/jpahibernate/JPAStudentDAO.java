package ttl.larku.dao.jpahibernate;

import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Student;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@Profile("production")
public class JPAStudentDAO implements BaseDAO<Student> {

    @PersistenceContext
    private EntityManager entityManager;


    public JPAStudentDAO() {
        int i = 0;
    }

    @Override
    public void update(Student updateObject) {
        entityManager.merge(updateObject);
    }

    @Override
    public void delete(Student objToDelete) {
        Student managed = entityManager.find(Student.class, objToDelete.getId());
        entityManager.remove(managed);
    }

    @Override
    public Student create(Student newObject) {
        entityManager.persist(newObject);
        return newObject;
    }

    @Override
    public Student get(int id) {
        return entityManager.find(Student.class, id);
    }

    @Override
    public List<Student> getAll() {
//        TypedQuery<Student> query =
//                entityManager.createQuery("select s from Student s left join fetch s.classes sc left join fetch sc.course" , Student.class);
        TypedQuery<Student> query = entityManager.createQuery("Select s from Student s", Student.class);
        List<Student> students = query.getResultList();
        return students;
    }

    @Override
    public void deleteStore() {
        Query query = entityManager.createQuery("Delete from Student");
        query.executeUpdate();
    }

    @Override
    public void createStore() {
    }

}