package com.senla.app.task.db.dao.hibernate_implementations;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.app.task.db.DatabaseException;
import com.senla.app.task.db.dao.AbstractHibernateDao;
import com.senla.app.task.model.entity.Book;
import com.senla.app.task.model.entity.sortby.BookSortBy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Hibernate
public class HibernateBookDao extends AbstractHibernateDao<Book, Integer, BookSortBy> {

    private final Map<BookSortBy, String> aliasesForSortBy = Map.of(
            BookSortBy.TITLE,               "b.title",
            BookSortBy.PUBLICATION_DATE,    "b.publicationDate",
            BookSortBy.ADMISSION_DATE,      "b.admissionDate",
            BookSortBy.PRICE,               "b.price",
            BookSortBy.AVAILABILITY,        "b.status",
            BookSortBy.DATE_PRICE,          "b.admissionDate, b.price"
    );

    public HibernateBookDao() {
        super(Book.class);
    }

    @Override
    protected String getEntityName() {
        return "books";
    }

    @Override
    protected String getEntityAlias() {
        return "b";
    }

    @Override
    protected String getAliasesForSortBy(BookSortBy sortBy) {
        String result = aliasesForSortBy.get(sortBy);

        if (result == null)
            throw new DatabaseException("Данный вид сортировки не поддерживается");
        return result;
    }

    @Override
    protected String additionalJoinFetchQuery() {
        return "LEFT JOIN FETCH b.order";
    }
}
