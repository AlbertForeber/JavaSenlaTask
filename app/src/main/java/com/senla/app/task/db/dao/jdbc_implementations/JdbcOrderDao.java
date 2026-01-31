package com.senla.app.task.db.dao.jdbc_implementations;

import com.senla.annotation.db_qualifiers.Jdbc;
import com.senla.app.task.db.DatabaseException;
import com.senla.app.task.db.dao.AbstractJdbcDao;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.Order;
import com.senla.app.task.model.entity.sortby.OrderSortBy;
import com.senla.app.task.model.entity.status.BookStatus;
import com.senla.app.task.model.entity.status.OrderStatus;
import com.senla.app.task.utils.DateConverter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Jdbc
public class JdbcOrderDao extends AbstractJdbcDao<Order, Integer, OrderSortBy> {

    private final Map<Integer, Book> bookCache = new HashMap<>();

    private final Map<OrderSortBy, String> aliasesForSortBy = Map.of(
            OrderSortBy.PRICE,              "o.total_sum",
            OrderSortBy.STATUS,             "o.status",
            OrderSortBy.COMPLETION_DATE,    "o.completion_date",
            OrderSortBy.PRICE_DATE,         "o.total_sum, o.completion_date"
    );

    @Override
    protected final void clearCache() {
        bookCache.clear();
    }

    @Override
    protected final String selectedFields() {
        return "o.*, JSONB_AGG(b.*) AS ordered_books";
    }

    @Override
    protected final String additionalGroupQuery() {
        return "GROUP BY o.id";
    }

    @Override
    protected final String additionalJoinQuery() {
        return "JOIN books b ON o.id = b.order_id";
    }

    @Override
    protected String getTableName() {
        return "orders";
    }

    @Override
    protected String getTableAlias() {
        return "o";
    }

    @Override
    protected String getAliasesForSortBy(OrderSortBy sortBy) {
        try {
            return aliasesForSortBy.get(sortBy);
        } catch (NullPointerException e) {
            throw new DatabaseException("Данный вид сортировки не поддерживается");
        }
    }

    @Override
    protected Order toEntity(ResultSet resultSet) throws SQLException {
        // Заполним позже
        List<Book> books = new ArrayList<>();

        Order order = new Order(
                resultSet.getInt("id"),
                books,
                resultSet.getString("customer_name"),
                resultSet.getInt("total_sum"),
                DateConverter.dateSqlToCalendar(resultSet.getDate("completion_date")),
                OrderStatus.valueOf(resultSet.getString("status"))
        );
        books.addAll(provideOrderedBooks(resultSet.getString("ordered_books"), order));

        return order;
    }

    @Override
    protected String getInsertFields(Order entity) {
        return "id, customer_name, total_sum, status, completion_date";
    }

    @Override
    protected int getFieldsCount(Order entity) {
        return 5;
    }

    @Override
    protected String generateUpdatePlaceholders(Order entity) {

        return "id = ?" + ", " +
                "customer_name = ?" + ", " +
                "total_sum = ?" + ", " +
                "status = ?" +
                ", " + "completion_date = ?";
    }

    @Override
    protected void setInsertValues(Order entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, entity.getId());
        preparedStatement.setString(2, entity.getCustomerName());
        preparedStatement.setInt(3, entity.getTotalSum());
        preparedStatement.setString(4, entity.getStatus().name());
        preparedStatement
                .setDate(
                        5, entity.getCompletionDate() != null ?
                                Date.valueOf(DateConverter.calendarToLocalDate(entity.getCompletionDate())) : null);
    }

    @Override
    protected Integer getIdOf(Order entity) {
        return entity.getId();
    }

    private List<Book> provideOrderedBooks(String orderedBooks, Order order) {
        List<Book> orderedBooksList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(orderedBooks);

        jsonArray.forEach(o -> {
                JSONObject jsonObject = (JSONObject) o;
                int orderId = jsonObject.getInt("id");

                if (bookCache.containsKey(orderId)) {
                    orderedBooksList.add(bookCache.get(orderId));
                } else {
                    Book book = new Book(
                            orderId,
                            jsonObject.getString("title"),
                            jsonObject.getString("description"),
                            DateConverter.jsonStringToCalendar(jsonObject.getString("publication_date")),
                            DateConverter.jsonStringToCalendar(jsonObject.getString("admission_date")),
                            jsonObject.getInt("price"),
                            BookStatus.valueOf(jsonObject.getString("status")),
                            order
                    );

                    bookCache.put(book.getId(), book);
                    orderedBooksList.add(book);
                }
            }
        );

        return orderedBooksList;
    }
}
