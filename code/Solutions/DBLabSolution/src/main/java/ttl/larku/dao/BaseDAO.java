package ttl.larku.dao;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @param <T>
 * @author anil
 */
public interface BaseDAO<T> {

    public void update(T updateObject);

    public void delete(T deleteObject);

    public T create(T newObject);

    public T get(int id);

    public List<T> getAll();

    public default void deleteStore() {
        throw new UnsupportedOperationException("DeleteStore not Supported");
    }

    public default void createStore() {
        throw new UnsupportedOperationException("CreateStore not Supported");
    }

    /**
     * Implement default findBy functionality for all DAOs.
     * Aren't we nice.
     */
    public default List<T> findBy(Predicate<T> pred) {
        List<T> result = getAll()
                .stream()
                .filter(pred)
                .collect(Collectors.toList());

        return result;
    }
}
