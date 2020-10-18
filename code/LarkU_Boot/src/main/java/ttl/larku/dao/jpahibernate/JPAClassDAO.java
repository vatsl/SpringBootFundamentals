package ttl.larku.dao.jpahibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.ScheduledClass;

@Repository
@Transactional
@Profile("production")
public class JPAClassDAO implements BaseDAO<ScheduledClass> {

    @PersistenceContext //(unitName="LarkUPU_SE")
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void update(ScheduledClass updateObject) {
        entityManager.merge(updateObject);
    }

    @Override
    public void delete(ScheduledClass objToDelete) {
        ScheduledClass managed = entityManager.find(ScheduledClass.class, objToDelete.getId());
        entityManager.remove(managed);
    }

    @Override
    public ScheduledClass create(ScheduledClass newObject) {
        entityManager.persist(newObject);
        return newObject;
    }

    @Override
    public ScheduledClass get(int id) {
        return entityManager.find(ScheduledClass.class, id);
    }

    @Override
    public List<ScheduledClass> getAll() {
        TypedQuery<ScheduledClass> query = entityManager.
                createQuery("Select s from ScheduledClass s", ScheduledClass.class);
        List<ScheduledClass> classes = query.getResultList();
        return classes;
    }

    @Override
    public void deleteStore() {
        Query query = entityManager.createQuery("Delete from ScheduledClass");
        query.executeUpdate();
    }

    @Override
    public void createStore() {
    }
}
