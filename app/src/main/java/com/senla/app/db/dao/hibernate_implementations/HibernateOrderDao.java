package com.senla.app.db.dao.hibernate_implementations;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.app.db.DatabaseException;
import com.senla.app.db.dao.AbstractHibernateDao;
import com.senla.app.model.entity.Order;
import com.senla.app.model.entity.sortby.OrderSortBy;
import jakarta.persistence.criteria.JoinType;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Hibernate
public class HibernateOrderDao extends AbstractHibernateDao<Order, Integer, OrderSortBy> {

    private final Map<OrderSortBy, String> aliasesForSortBy = Map.of(
            OrderSortBy.PRICE,              "o.totalSum",
            OrderSortBy.STATUS,             "o.status",
            OrderSortBy.COMPLETION_DATE,    "o.completionDate",
            OrderSortBy.PRICE_DATE,         "o.totalSum, o.completionDate"
    );

    public HibernateOrderDao(SessionFactory factory) {
        super(Order.class, factory);
    }

    @Override
    protected String getEntityName() {
        return "orders";
    }

    @Override
    protected String getEntityAlias() {
        return "o";
    }

    @Override
    protected String getAliasesForSortBy(OrderSortBy sortBy) {
        String result = aliasesForSortBy.get(sortBy);

        if (result == null)
            throw new DatabaseException("Данный вид сортировки не поддерживается");
        return result;
    }

    @Override
    protected String additionalJoinFetchQueryHql() {
        return "LEFT JOIN FETCH o.orderedBooks";
    }

    @Override
    protected Map.Entry<String, JoinType> additionalJoinFetchQueryCriteria() {
        return Map.entry("orderedBooks", JoinType.LEFT);
    }
}
