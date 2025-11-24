package service;

import java.util.List;
import java.util.Optional;

public interface DataService<T, ID> {
    List<T> getAll();
    Optional<T> getById(ID id);
    T save(T entity);
    void delete(ID id);
    long count();
}
