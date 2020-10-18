package ttl.larku.dao.jpahibernate;

import org.springframework.transaction.annotation.Transactional;
import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Track;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
public class JPATrackDAO implements BaseDAO<Track> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void update(Track updateObject) {
        em.merge(updateObject);
    }

    @Override
    public void delete(Track deleteObject) {
        em.remove(deleteObject);
    }

    @Override
    public Track create(Track newObject) {
        em.persist(newObject);
        return newObject;
    }

    @Override
    public Track get(int id) {
        return em.find(Track.class, id);
    }

    @Override
    public List<Track> getAll() {
        TypedQuery<Track> query = em.createQuery("Select t from Track t", Track.class);
        List<Track> tracks = query.getResultList();
        return tracks;
    }

    @Override
    public void deleteStore() {
        Query query = em.createQuery("Delete from Track");
        query.executeUpdate();
    }

    @Override
    public void createStore() {
    }
}
