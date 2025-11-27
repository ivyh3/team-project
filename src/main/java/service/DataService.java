package service;

import java.util.List;
import java.util.Optional;

/**
 * Generic data service for CRUD operations on a given entity type.
 *
 * @param <T>  the entity type
 * @param <ID> the type of the entity ID
 */
public interface DataService<T, ID> {

    /** Returns all entities. */
    List<T> getAll();

    /** Returns an entity by ID, or empty if not found. */
    Optional<T> getById(ID id);

    /** Saves (or updates) an entity and returns the saved entity. */
    T save(T entity);

    /** Deletes an entity by ID. */
    void delete(ID id);

    /** Returns the total number of entities. */
    long count();
}
