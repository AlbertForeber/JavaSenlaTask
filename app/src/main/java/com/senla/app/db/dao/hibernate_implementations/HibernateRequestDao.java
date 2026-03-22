package com.senla.app.db.dao.hibernate_implementations;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.app.db.DatabaseException;
import com.senla.app.db.dao.AbstractHibernateDao;
import com.senla.app.model.entity.Request;
import com.senla.app.model.entity.sortby.RequestSortBy;
import jakarta.persistence.criteria.JoinType;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Hibernate
public class HibernateRequestDao extends AbstractHibernateDao<Request, Integer, RequestSortBy> {

    private final Map<RequestSortBy, String> aliasesForSortBy = Map.of(
            RequestSortBy.AMOUNT,       "r.amount",
            RequestSortBy.BOOK_NAME,    "b.title"
    );

    public HibernateRequestDao(SessionFactory factory) {
        super(Request.class, factory);
    }

    @Override
    protected String getEntityName() {
        return "requests";
    }

    @Override
    protected String getEntityAlias() {
        return "r";
    }

    @Override
    protected String getAliasesForSortBy(RequestSortBy sortBy) {
        String result = aliasesForSortBy.get(sortBy);

        if (result == null)
            throw new DatabaseException("Данный вид сортировки не поддерживается");
        return result;
    }

    @Override
    protected String additionalJoinFetchQueryHql() {
        return "JOIN FETCH r.book b";
    }

    @Override
    protected Map.Entry<String, JoinType> additionalJoinFetchQueryCriteria() {
        return Map.entry("book", JoinType.INNER);
    }
}
