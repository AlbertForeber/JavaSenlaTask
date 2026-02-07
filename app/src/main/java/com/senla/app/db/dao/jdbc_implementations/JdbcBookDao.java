package com.senla.app.db.dao.jdbc_implementations;

import com.senla.annotation.db_qualifiers.Jdbc;
import com.senla.app.db.DatabaseException;
import com.senla.app.db.dao.AbstractJdbcDao;
import com.senla.app.model.entity.Book;
import com.senla.app.model.entity.Order;
import com.senla.app.model.entity.sortby.BookSortBy;
import com.senla.app.model.entity.status.BookStatus;
import com.senla.app.model.entity.status.OrderStatus;
import com.senla.app.utils.DateConverter;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
@Jdbc
public class JdbcBookDao extends AbstractJdbcDao<Book, Integer, BookSortBy> {

    private final Map<Integer, Order> orderCache = new HashMap<>();

    private final Map<BookSortBy, String> aliasesForSortBy = Map.of(
            BookSortBy.TITLE,               "b.title",
            BookSortBy.PUBLICATION_DATE,    "b.publication_date",
            BookSortBy.ADMISSION_DATE,      "b.admission_date",
            BookSortBy.PRICE,               "b.price",
            BookSortBy.AVAILABILITY,        "b.status",
            BookSortBy.DATE_PRICE,          "b.admission_date, b.price"
    );

    @Override
    protected final void clearCache() {
        orderCache.clear();
    }

    public JdbcBookDao() {
        super();
    }

    @Override
    protected String getTableName() {
        return "books";
    }

    @Override
    protected String getTableAlias() {
        return "b";
    }

    @Override
    protected String getAliasesForSortBy(BookSortBy sortBy) {
        try {
            return aliasesForSortBy.get(sortBy);
        } catch (NullPointerException e) {
            throw new DatabaseException("Данный вид сортировки не поддерживается");
        }
    }

    @Override
    protected String selectedFields() {
        return "b.*, " +
                "o.id AS oid, " +
                "o.customer_name AS ocustomer_name, " +
                "o.total_sum AS ototal_sum, " +
                "o.completion_date AS ocompletion_date, " +
                "o.status AS ostatus";
    }

    @Override
    protected Book toEntity(ResultSet resultSet) throws SQLException {
        Order order = provideOrder(resultSet);

        return new Book(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                DateConverter.dateSqlToCalendar(resultSet.getDate("publication_date")),
                DateConverter.dateSqlToCalendar(resultSet.getDate("admission_date")),
                resultSet.getInt("price"),
                BookStatus.valueOf(resultSet.getString("status")),
                order
        );
    }

    @Override
    protected String getInsertFields(Book entity) {
        return "id, title, description, publication_date, admission_date, price, status, order_id";
    }

    @Override
    protected int getFieldsCount(Book entity) {
        return 8;
    }

    @Override
    protected String generateUpdatePlaceholders(Book entity) {

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
    protected void setInsertValues(Book entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, entity.getId());
        preparedStatement.setString(2, entity.getTitle());
        preparedStatement.setString(3, entity.getDescription());

        preparedStatement
                .setDate(4, Date.valueOf(DateConverter.calendarToLocalDate(entity.getPublicationDate())));

        preparedStatement
                .setDate(5, Date.valueOf(DateConverter.calendarToLocalDate(entity.getAdmissionDate())));

        preparedStatement.setInt(6, entity.getPrice());
        preparedStatement.setString(7, entity.getStatus().name());

        if (entity.getOrderId() != 0)
            preparedStatement.setInt(8, entity.getOrderId());
        else
            preparedStatement.setObject(8, null);
    }

    @Override
    protected Integer getIdOf(Book entity) {
        return entity.getId();
    }

    @Override
    protected String additionalJoinQuery() {
        return "LEFT JOIN orders o ON b.order_id=o.id";
    }

    private Order provideOrder(ResultSet resultSet) throws SQLException {
        int orderId = resultSet.getInt("order_id");
        if (orderId == 0) return null;

        Date orderCompletionDate = resultSet.getDate("ocompletion_date");
        Order order = orderCache.getOrDefault(orderId, null);

        if (order == null) {
            order = new Order(
                    orderId,
                    null,       // Для книги не получаем книги самого заказа - избыточная информация
                    resultSet.getString("ocustomer_name"),
                    resultSet.getInt("ototal_sum"),
                    orderCompletionDate != null ? DateConverter.dateSqlToCalendar(orderCompletionDate) : null,
                    OrderStatus.valueOf(resultSet.getString("ostatus"))
            );

            orderCache.put(orderId, order);
        }

        return order;
    }
}
