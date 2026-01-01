package com.senla.app.task.db.dao.implementations;

import com.senla.app.task.db.dao.AbstractGenericDao;
import com.senla.app.task.model.dto.RequestDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestDao extends AbstractGenericDao<RequestDto, Integer> {
    @Override
    protected String getTableName() {
        return "requests";
    }

    @Override
    protected RequestDto toEntity(ResultSet resultSet) throws SQLException {
        return new RequestDto(
                resultSet.getInt("id"),
                resultSet.getInt("book_id"),
                resultSet.getString("title"),
                resultSet.getInt("amount")
        );
    }

    @Override
    protected String getInsertFields(RequestDto entity) {
        return entity.getId() != null ? "book_id, amount, id" : "book_id, amount";
    }

    @Override
    protected int getFieldsCount(RequestDto entity) {
        return entity.getId() != null ? 3 : 2;
    }

    @Override
    protected String generateUpdatePlaceholders(RequestDto entity) {
        return "book_id = ?, amount = ?, id = ?";
    }

    @Override
    protected void setInsertValues(RequestDto entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, entity.getBookId());
        preparedStatement.setInt(2, entity.getAmount());
        if (entity.getId() != null) preparedStatement.setInt(3, entity.getId());
    }

    @Override
    protected Integer getIdOf(RequestDto entity) {
        return entity.getId();
    }

    @Override
    protected String additionalJoinQuery() {
        return "JOIN books ON requests.book_id=books.id";
    }
}
