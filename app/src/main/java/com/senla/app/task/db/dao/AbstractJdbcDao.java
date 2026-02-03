package com.senla.app.task.db.dao;

import com.senla.app.task.db.DatabaseException;
import com.senla.app.task.db.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractJdbcDao<T, ID, SB> implements GenericDao<T, ID, SB> {

    protected final Connection connection = DbConnection.getInstance().initOrGetConnection();

    protected abstract String getTableName();

    protected abstract T toEntity(ResultSet resultSet) throws SQLException;

    protected abstract String getInsertFields(T entity);

    protected abstract int getFieldsCount(T entity);

    protected abstract String generateUpdatePlaceholders(T entity);

    protected abstract void setInsertValues(T entity, PreparedStatement preparedStatement) throws SQLException;

    protected abstract ID getIdOf(T entity);

    protected abstract String getTableAlias();

    protected abstract String getAliasesForSortBy(SB sortBy);

    protected String additionalJoinQuery() {
        return "";
    };

    protected String additionalGroupQuery() {
        return "";
    }

    protected String selectedFields() {
        return "*";
    }

    protected void clearCache() { }

    @Override
    public T findById(ID id, boolean useJoin) throws DatabaseException {
        try (
                PreparedStatement preparedStatement = connection
                        .prepareStatement(
                                "SELECT " + selectedFields() + " FROM " + getTableName() + " " + getTableAlias() + " "
                                        + additionalJoinQuery()
                                        + " WHERE " + getTableAlias() + ".id = ? "
                                        + additionalGroupQuery()
                        )
        ) {
            // PreparedStatement умеет подставлять в плейсхолдеры только внутри WHERE, INSERT, UPDATE, DELETE
            // Имя таблицы им не выбрать
//            preparedStatement.setString(1, getTableName());
            preparedStatement.setObject(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // По умолчанию курсор ResultSet указывает на позицию
                // до первой строки результата
                // первый вызов .next перемещает курсор на первую строку
                if (resultSet.next()) {
                    return toEntity(resultSet);
                }

                return null;
            } finally {
                clearCache();
            }

        // Отлавливаем ошибки БД и переводим их в кастомные для консистентности
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public List<T> findAll(SB sortBy, boolean useJoin) throws DatabaseException {
        try (
                PreparedStatement preparedStatement = connection
                        .prepareStatement(
                                "SELECT " + selectedFields() + " FROM " + getTableName() + " " + getTableAlias() + " "
                                + additionalJoinQuery()
                                + " " + additionalGroupQuery()
                                /*+ (sortBy != null ? " ORDER BY " + String.join(", ", sortBy) : "")*/
                                )

        ) {
            /*
            Аналогичная проблема
            preparedStatement.setString(1, getTableName());
            System.out.println("SELECT * FROM " + getTableName() + " " + additionalJoinQuery());
            */

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<T> entities = new ArrayList<>();

                // Аналогично
                while (resultSet.next()) {
                    entities.add(toEntity(resultSet));
                }

                return entities;
            } finally {
                clearCache();
            }
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public void save(T entity) throws DatabaseException {
        String query = "INSERT INTO " + getTableName() + "(" + getInsertFields(entity) + ")" + " VALUES (" + generatePlaceholders(getFieldsCount(entity)) + ")";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            setInsertValues(entity, preparedStatement);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public void update(T entity) throws DatabaseException {
        String query = "UPDATE " + getTableName() + " SET " + generateUpdatePlaceholders(entity) + " WHERE id=?";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            setInsertValues(entity, preparedStatement);
            preparedStatement.setObject(getFieldsCount(entity) + 1, getIdOf(entity));
            int affected = preparedStatement.executeUpdate();

            if (affected == 0) {
                throw new SQLException("Ошибка обновления - сущность не найдена");
            }
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public void delete(ID id) throws DatabaseException {
        try (
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + getTableName() + "WHERE id=?")
        ) {
            preparedStatement.setObject(1, id);
            int affected = preparedStatement.executeUpdate();

            if (affected == 0) {
                throw new SQLException("Ошибка удаления - сущность не найдена");
            }
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private String generatePlaceholders(int count) {
        return String.join(", ", Collections.nCopies(count, "?"));
    }
}
