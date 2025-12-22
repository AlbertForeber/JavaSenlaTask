package com.senla.app.task.db.dao.implementations;

import com.senla.app.task.db.dao.AbstractGenericDao;
import com.senla.app.task.db.dao.dto.BookDto;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.status.BookStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BookDao extends AbstractGenericDao<BookDto, Integer> {
    @Override
    protected String getTableName() {
        return "book";
    }

    @Override
    protected BookDto toEntity(ResultSet resultSet) throws SQLException {

        LocalDate admissionDate = resultSet.getDate("admission_date").toLocalDate();
        LocalDate publicationDate = resultSet.getDate("publication_date").toLocalDate();
        BookStatus bookStatus = switch (resultSet.getString("status")) {
            case "free" -> BookStatus.FREE;
            case "reserved" -> BookStatus.RESERVED;
            case "sold_out" -> BookStatus.SOLD_OUT;
            default -> throw new SQLException("Строка таблицы user содержит неверное состояние");
        };

        Book toReturn = new Book(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                admissionDate.getYear(),
                admissionDate.getMonth().getValue(),
                admissionDate.getDayOfMonth(),
                bookStatus
        );

        toReturn.setPrice(resultSet.getInt("price"));
        toReturn.setPublicationDate(
                publicationDate.getYear(),
                publicationDate.getMonth().getValue(),
                publicationDate.getDayOfMonth()
        );

        return toReturn;
    }

    @Override
    protected String getInsertFields() {
        return "id, title, description";
    }

    @Override
    protected String getInsertValues(Book entity) {
        return "";
    }

    @Override
    protected String formUpdateClause(Book entity) {
        return "";
    }

    @Override
    protected int getIdOf(Book entity) {
        return 0;
    }
}
