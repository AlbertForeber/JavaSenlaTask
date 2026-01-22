package com.senla.app.task.db.dao.hibernate_implementations;

import com.senla.app.task.db.DatabaseException;
import com.senla.app.task.db.dao.AbstractHibernateDao;
import com.senla.app.task.model.entity.Request;
import com.senla.app.task.model.entity.sortby.RequestSortBy;

import java.util.Map;

public class RequestDao extends AbstractHibernateDao<Request, Integer, RequestSortBy> {

    private final Map<RequestSortBy, String> aliasesForSortBy = Map.of(
            RequestSortBy.AMOUNT,       "r.amount",
            RequestSortBy.BOOK_NAME,    "b.title"
    );

    public RequestDao() {
        super(Request.class);
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
    protected String additionalJoinFetchQuery() {
        return "JOIN FETCH r.book b";
    }
}
