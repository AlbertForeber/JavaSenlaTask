package com.senla.app.task.db.dao;

import com.senla.app.task.db.DbConnection;
import com.senla.app.task.model.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGenericDao<T, ID> implements GenericDao<T, ID> {
    protected final Connection connection = DbConnection.getConnection();

    protected abstract String getTableName();

    protected abstract T toEntity(ResultSet resultSet) throws SQLException;

    protected abstract String getInsertFields();

    protected abstract String getInsertValues(T entity);

    protected abstract String formUpdateClause(T entity);

    protected abstract ID getIdOf(T entity);

    @Override
    public T findById(ID id) throws SQLException {
        try (
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ? WHERE id = ?")
        ) {
            preparedStatement.setString(1, getTableName());
            preparedStatement.setObject(2, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // По умолчанию курсор ResultSet указывает на позицию
                // до первой строки результата
                // первый вызов .next перемещает курсор на первую строку
                if (resultSet.next()) {
                    return toEntity(resultSet);
                }

                return null;
            }
        }
    }

    @Override
    public List<T> findAll() throws SQLException {
        try (
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ?")
        ) {
            preparedStatement.setString(1, getTableName());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<T> entities = new ArrayList<>();

                // Аналогично
                while (resultSet.next()) {
                    entities.add(toEntity(resultSet));
                }

                return entities;
            }
        }
    }

    @Override
    public void add(T entity) throws SQLException {
        String query = "INSERT INTO " + getTableName() + "(" + getInsertFields() + ")" + "VALUES (" + getInsertValues(entity) + ")";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void update(T entity) throws SQLException {
        String query = "UPDATE " + getTableName() + "SET " + formUpdateClause(entity) + "WHERE id=?";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setObject(1, getIdOf(entity));
            int affected = preparedStatement.executeUpdate();

            if (affected == 0) {
                throw new SQLException("Ошибка обновления - сущность не найдена");
            }
        }
    }

    @Override
    public void delete(ID id) throws SQLException {
        try (
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + getTableName() + "WHERE id=?")
        ) {
            preparedStatement.setObject(1, id);
            int affected = preparedStatement.executeUpdate();

            if (affected == 0) {
                throw new SQLException("Ошибка удаления - сущность не найдена");
            }
        }
    }

    protected abstract String getInsertValues(Book entity);
}
