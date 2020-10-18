package ttl.larku.dao;

import java.util.List;

/**
 * @param <T>
 * @author anil
 */
public interface CopyOfBaseDAO<T> {

    public void update(T updateObject);

    public void delete(int id);

    public int create(T newObject);

    public T get(int id);

    public List<T> getAll();

    public void deleteStore();

    public void createStore();
}
