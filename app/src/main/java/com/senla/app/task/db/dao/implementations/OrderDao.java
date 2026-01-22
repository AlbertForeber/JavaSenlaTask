package com.senla.app.task.db.dao.implementations;

import com.senla.app.task.db.dao.AbstractGenericDao;
import com.senla.app.task.model.dto.OrderDto;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDao extends AbstractGenericDao<OrderDto, Integer> {

    @Override
    protected String getTableName() {
        return "orders";
    }

    @Override
    protected OrderDto toEntity(ResultSet resultSet) throws SQLException {
        return new OrderDto(
                resultSet.getInt("id"),
                resultSet.getString("customer_name"),
                resultSet.getInt("total_sum"),
                resultSet.getDate("completion_date") != null ? resultSet.getDate("completion_date").toLocalDate() : null,
                resultSet.getString("status")
        );
    }

    @Override
    protected String getInsertFields(OrderDto entity) {
        return "id, customer_name, total_sum, status, completion_date";
    }

    @Override
    protected int getFieldsCount(OrderDto entity) {
        return 5;
    }

    @Override
    protected String generateUpdatePlaceholders(OrderDto entity) {

        return "id = ?" + ", " +
                "customer_name = ?" + ", " +
                "total_sum = ?" + ", " +
                "status = ?" +
                ", " + "completion_date = ?";
    }

    @Override
    protected void setInsertValues(OrderDto entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, entity.getId());
        preparedStatement.setString(2, entity.getCustomerName());
        preparedStatement.setInt(3, entity.getTotalSum());
        preparedStatement.setString(4, entity.getStatus());
        preparedStatement
                .setDate(5, entity.getCompletionDate() != null ? Date.valueOf(entity.getCompletionDate()) : null);
    }

    @Override
    protected Integer getIdOf(OrderDto entity) {
        return entity.getId();
    }

    @Override
    protected String additionalJoinQuery() {
        return "";
    }
}
