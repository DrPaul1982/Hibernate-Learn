package org.example;

import java.util.List;

public interface GenericDAO<T, ID> {

    void save(T entity);

    T findByID(ID id);

    T findByEmail(String email);

    List<T> findAll();

    Student update(T entity);

    boolean deleteById(ID id);

}
