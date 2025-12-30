package com.senla.app.task.db.dao.implementations;

import com.senla.app.task.db.dao.AbstractGenericDao;
import com.senla.app.task.repository.dto.BookDto;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDao extends AbstractGenericDao<BookDto, Integer> {

    public BookDao() {
        super();
    }

    @Override
    protected String getTableName() {
        return "book";
    }

    @Override
    protected BookDto toEntity(ResultSet resultSet) throws SQLException {
        return new BookDto(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getDate("publication_date").toLocalDate(),
                resultSet.getDate("admission_date").toLocalDate(),
                resultSet.getInt("price"),
                resultSet.getString("status"),
                resultSet.getString("reservist"),
                resultSet.getInt("order_id")
        );
    }

    @Override
    protected String getInsertFields(BookDto entity) {
        StringBuilder stringBuilder = new StringBuilder("id, title, description, publication_date, admission_date, price, status");
        if (entity.getReservist() != null) stringBuilder.append(", ").append(", reservist");
        if (entity.getOrderId() != null) stringBuilder.append(", ").append(", order_id");

        return stringBuilder.toString();
    }

    @Override
    protected int getFieldsCount(BookDto entity) {
        int basicNumber = 7;

        if (entity.getReservist() != null) basicNumber ++;
        if (entity.getOrderId() != null) basicNumber ++;

        return basicNumber;
    }

    @Override
    protected String generateUpdatePlaceholders(BookDto entity) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id = ?").append(", ");
        stringBuilder.append("title = ?").append(", ");
        stringBuilder.append("description = ?").append(", ");
        stringBuilder.append("publication_date = ?").append(", ");
        stringBuilder.append("admission_date = ?").append(", ");
        stringBuilder.append("price = ?").append(", ");
        stringBuilder.append("status = ?").append(", ");

        if (entity.getReservist() != null) stringBuilder.append("reservist").append(", ");
        if (entity.getOrderId() != null) stringBuilder.append("status").append(", ");

        return stringBuilder.toString();
    }

    @Override
    protected void setInsertValues(BookDto entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, entity.getId());
        preparedStatement.setString(2, entity.getTitle());
        preparedStatement.setString(3, entity.getDescription());
        preparedStatement.setDate(4, Date.valueOf(entity.getPublicationDate()));
        preparedStatement.setDate(5, Date.valueOf(entity.getAdmissionDate()));
        preparedStatement.setInt(6, entity.getPrice());
        preparedStatement.setString(7, entity.getStatus());
    }

    @Override
    protected Integer getIdOf(BookDto entity) {
        return entity.getId();
    }

}
