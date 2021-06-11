package model.mapper;

import java.util.List;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapper<T> {
    T fromResultSet(ResultSet resultSet) throws SQLException;
    void fillPreparedStatement(T entity, PreparedStatement preparedStatement, List<String> columnNames) throws SQLException;
}