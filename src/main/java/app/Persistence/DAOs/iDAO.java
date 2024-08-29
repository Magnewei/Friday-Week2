package app.Persistence.DAOs;

import java.util.Set;

public interface iDAO<T> {
    boolean create(T type);
    boolean delete(T type);
    T getById(long id);
    Set<T> getAll();
    boolean update(T student);
}
