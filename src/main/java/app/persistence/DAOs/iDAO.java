package app.persistence.DAOs;

import java.util.Set;

public interface iDAO<T> {
    boolean create(T type);
    boolean delete(T type);
    T getById(T type);
    Set<T> getAll();
    boolean update(T student);
}
