package com.senla.app.task.db.dao.hibernate_implementations;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.app.task.db.DatabaseException;
import com.senla.app.task.db.dao.AbstractHibernateDao;
import com.senla.app.task.model.entity.Order;
import com.senla.app.task.model.entity.sortby.OrderSortBy;
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

    public HibernateOrderDao() {
        super(Order.class);
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
    protected String additionalJoinFetchQuery() {
        return "LEFT JOIN FETCH o.orderedBooks";
    }
}
