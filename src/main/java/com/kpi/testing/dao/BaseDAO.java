package com.kpi.testing.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BaseDAO<T> extends AutoCloseable{
    void create (T entity);
    Optional<T> findById(Long id);
    List<T> findAll();
    void update(T entity);
    void delete(Long id);
}
