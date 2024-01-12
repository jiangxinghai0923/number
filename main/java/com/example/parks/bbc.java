package com.example.parks;

import java.sql.SQLException;
import java.util.List;

public interface bbc<T,K> {
    void insert(T entity) throws SQLException;

    void insertBatch(List<T> entities) throws SQLException;

    void updateByid(T entity, String primaryKey) throws SQLException;

    void updateByCondition(String condition, Object[]params, T entity) throws SQLException;
    void delete(K primaryKey)throws SQLException;

    void deleteBatch(List<K> primaryKeys) throws  SQLException;
    void deleteByCondition(String condition,Object[]params) throws SQLException;

    T select(K primaryKey) throws SQLException;

    List<T> selectAll() throws SQLException;
    List<T> selectByCondition(String conditions) throws SQLException;

    List<T> selectPaged(int page, int pagesize)throws SQLException;

    int gettotalRecords() throws Exception;
}

