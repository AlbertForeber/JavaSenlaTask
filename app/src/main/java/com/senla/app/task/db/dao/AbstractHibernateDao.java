package com.senla.app.task.db.dao;

import com.senla.app.task.utils.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

import java.sql.SQLException;
import java.util.List;

public class AbstractHibernateDao<T, PK> implements GenericDao<T, PK> {

    private Class<T> type;

    @Override
    public T findById(PK pk) throws SQLException {
        return HibernateUtil.getSession().get(type, pk);
    }

    @Override
    public List<T> findAll(List<String> sortBy) throws SQLException {

        CriteriaBuilder criteriaBuilder = HibernateUtil.getSession().getCriteriaBuilder();
        CriteriaQuery<T> cq = criteriaBuilder.createQuery(type);
        Root<T> root = cq.from(type);

        List<Order> orders = sortBy.stream().map(o -> criteriaBuilder.asc(root.get(o))).toList();

        cq.orderBy(orders);
        // root.fetch()

        return HibernateUtil.getSession().createQuery(cq).getResultList();
    }

    @Override
    public void save(T entity) throws SQLException {

    }

    @Override
    public void update(T entity) throws SQLException {

    }

    @Override
    public void delete(PK pk) throws SQLException {

    }
}
