package com.senla.app.db.dao;

import com.senla.app.db.DbConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.senla.app.exceptions.DataManipulationException;

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

    protected abstract void setId(T entity, int id);

    protected void clearCache() { }

    @Override
    public T findById(ID id, boolean useJoin) {
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
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public List<T> findAll(SB sortBy, boolean useJoin) {
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
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public T save(T entity) {
        String query = "INSERT INTO " + getTableName() + "(" + getInsertFields(entity) + ")" + " VALUES (" + generatePlaceholders(getFieldsCount(entity)) + ")";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            setInsertValues(entity, preparedStatement);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Устанавливаем ID в объект (вам нужно будет реализовать метод setId)
                    setId(entity, generatedKeys.getInt(1));
                } else {
                    throw new DataManipulationException("Creating entity failed, no ID obtained.");
                }
            }

            return entity;
        } catch (Exception e) {
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public void update(T entity) {
        String query = "UPDATE " + getTableName() + " SET " + generateUpdatePlaceholders(entity) + " WHERE id=?";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            setInsertValues(entity, preparedStatement);
            preparedStatement.setObject(getFieldsCount(entity) + 1, getIdOf(entity));
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public void delete(ID id) {
        try (
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + getTableName() + "WHERE id=?")
        ) {
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public List<T> findByField(String fieldName, Object value, boolean useJoin, boolean isJoinField) {
        throw new UnsupportedOperationException("Функция не реализована");
    }

    private String generatePlaceholders(int count) {
        return String.join(", ", Collections.nCopies(count, "?"));
    }
}
