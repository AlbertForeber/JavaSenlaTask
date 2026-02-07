package com.senla.app.db.dao;

import com.senla.app.db.DatabaseException;
import java.util.List;

// T - тип сущности
// ID - тип идентификатора
// SB - SortBy, тип enum класса, предоставляющего варианты сортировки
public interface GenericDao<T, ID, SB> {

    T findById(ID id, boolean useJoin) throws DatabaseException;
    List<T> findAll(SB sortBy, boolean useJoin) throws DatabaseException;
    void save(T entity) throws DatabaseException;
    void update(T entity) throws DatabaseException;
    void delete(ID id) throws DatabaseException;
}
