package dev.sam.scheduler.dao;

import org.xml.sax.SAXException;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
    List<T> getAll() throws SQLException;
    void insert(T t) throws SQLException;
    void update(T t, Integer itemId) throws SQLException;
    void delete(T t) throws SQLException;
}
