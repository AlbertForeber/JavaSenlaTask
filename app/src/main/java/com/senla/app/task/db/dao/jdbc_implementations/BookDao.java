package com.senla.app.task.db.dao.jdbc_implementations;

import com.senla.app.task.db.dao.AbstractJdbcDao;
import com.senla.app.task.model.dto.jdbc.BookDto;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDao extends AbstractJdbcDao<BookDto, Integer> {

    public BookDao() {
        super();
    }

    @Override
    protected String getTableName() {
        return "books";
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
                resultSet.getString("customer_name"),
                resultSet.getInt("order_id")
        );
    }

    @Override
    protected String getInsertFields(BookDto entity) {
        return "id, title, description, publication_date, admission_date, price, status, order_id";
    }

    @Override
    protected int getFieldsCount(BookDto entity) {
        return 8;
    }

    @Override
    protected String generateUpdatePlaceholders(BookDto entity) {

        return "id = ?" + ", " +
                "title = ?" + ", " +
                "description = ?" + ", " +
                "publication_date = ?" + ", " +
                "admission_date = ?" + ", " +
                "price = ?" + ", " +
                "status = ?" + ", " +
                "order_id = ?";
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
        if (entity.getOrderId() != null)
            preparedStatement.setInt(8, entity.getOrderId());
        else
            preparedStatement.setObject(8, null);
    }

    @Override
    protected Integer getIdOf(BookDto entity) {
        return entity.getId();
    }

    @Override
    protected String additionalJoinQuery() {
        return "LEFT JOIN orders ON books.order_id=orders.id";
    }
}
