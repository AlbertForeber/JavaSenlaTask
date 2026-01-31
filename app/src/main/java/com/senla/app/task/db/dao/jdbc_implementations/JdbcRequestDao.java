package com.senla.app.task.db.dao.jdbc_implementations;

import com.senla.annotation.db_qualifiers.Jdbc;
import com.senla.app.task.db.DatabaseException;
import com.senla.app.task.db.dao.AbstractJdbcDao;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.Request;
import com.senla.app.task.model.entity.sortby.RequestSortBy;
import com.senla.app.task.model.entity.status.BookStatus;
import com.senla.app.task.utils.DateConverter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Component
@Jdbc
public class JdbcRequestDao extends AbstractJdbcDao<Request, Integer, RequestSortBy> {

    private final Map<RequestSortBy, String> aliasesForSortBy = Map.of(
            RequestSortBy.AMOUNT,       "r.amount",
            RequestSortBy.BOOK_NAME,    "b.title"
    );

    // Тут кэш не нужен, т.к. все книги уникальны
    @Override
    protected String additionalJoinQuery() {
        return "JOIN books b ON requests.book_id=books.id";
    }

    @Override
    protected String selectedFields() {
        return "r.*, " +
                "b.id AS bid, " +
                "b.title AS btitle, " +
                "b.description AS bdescription, " +
                "publication_date AS bpublication_date, " +
                "admission_date AS badmission_date, " +
                "price AS bprice, " +
                "status AS bstatus";
    }

    @Override
    protected String getTableName() {
        return "requests";
    }

    @Override
    protected String getTableAlias() {
        return "r";
    }

    @Override
    protected String getAliasesForSortBy(RequestSortBy sortBy) {
        try {
            return aliasesForSortBy.get(sortBy);
        } catch (NullPointerException e) {
            throw new DatabaseException("Данный вид сортировки не поддерживается");
        }
    }

    @Override
    protected Request toEntity(ResultSet resultSet) throws SQLException {
        return new Request(
                resultSet.getInt("id"),
                new Book(
                        resultSet.getInt("bid"),
                        resultSet.getString("btitle"),
                        resultSet.getString("bdescription"),
                        DateConverter.dateSqlToCalendar(resultSet.getDate("bpublication_date")),
                        DateConverter.dateSqlToCalendar(resultSet.getDate("badmission_date")),
                        resultSet.getInt("bprice"),
                        BookStatus.valueOf(resultSet.getString("bstatus")),
                        null // Заказ не добавляем - избыточные данные
                ),
                resultSet.getInt("amount")
        );
    }

    @Override
    protected String getInsertFields(Request entity) {
        return entity.getId() != 0 ? "book_id, amount, id" : "book_id, amount";
    }

    @Override
    protected int getFieldsCount(Request entity) {
        return entity.getId() != 0 ? 3 : 2;
    }

    @Override
    protected String generateUpdatePlaceholders(Request entity) {
        return "book_id = ?, amount = ?, id = ?";
    }

    @Override
    protected void setInsertValues(Request entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, entity.getBook().getId());
        preparedStatement.setInt(2, entity.getAmount());
        if (entity.getId() != 0) preparedStatement.setInt(3, entity.getId());
    }

    @Override
    protected Integer getIdOf(Request entity) {
        return entity.getId();
    }
}
