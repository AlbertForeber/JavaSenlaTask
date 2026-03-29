package com.senla.db;

public interface SimpleGenericDao<T> {
    T findById(Integer id);
    T save(T entity);
}
