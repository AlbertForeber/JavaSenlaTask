package com.senla.app.db.dao;

import com.senla.app.db.DatabaseException;
import com.senla.app.exceptions.DataManipulationException;
import jakarta.persistence.NoResultException;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.util.List;

public abstract class AbstractHibernateDao<T, PK, SB> implements GenericDao<T, PK, SB> {

    private final Class<T> type;
    private final SessionFactory factory;
    private final String sql = "SELECT " + getEntityAlias()
            + " FROM " + getEntityName() + " " + getEntityAlias();

    protected abstract String getEntityName();

    protected abstract String getEntityAlias();

    protected abstract String getAliasesForSortBy(SB sortBy);

    protected String additionalJoinFetchQuery() {
        return "";
    }

    public AbstractHibernateDao(Class<T> type, SessionFactory factory) {
        this.type = type;
        this.factory = factory;
    }

    @Override
    public T findById(PK pk, boolean useJoin) {
        try {
            StringBuilder findByIdSql = new StringBuilder(sql);

            if (useJoin)
                findByIdSql.append(" ").append(additionalJoinFetchQuery());
            findByIdSql.append(" WHERE ").append(getEntityAlias()).append(".id = :id");

            return factory.getCurrentSession().createQuery(findByIdSql.toString(), type).setParameter("id", pk).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public List<T> findAll(SB sortBy, boolean useJoin) {
        try {
            factory.getCurrentSession().clear();

            StringBuilder findAllSql = new StringBuilder(sql);

            if (useJoin)
                findAllSql.append(" ").append(additionalJoinFetchQuery());

            if (sortBy != null) {
                findAllSql
                    .append(" ORDER BY ")
                    .append(
                        String.join(", ", getAliasesForSortBy(sortBy))
                    );
            }

            List<T> result = factory.getCurrentSession().createQuery(findAllSql.toString(), type).getResultList();

            if (result.isEmpty()) return null;

            return result;
/*
            CriteriaBuilder criteriaBuilder = factory.getCurrentSession().getCriteriaBuilder();
            CriteriaQuery<T> cq = criteriaBuilder.createQuery(type);
            Root<T> root = cq.from(type);

            if (sortBy != null) {
                List<Order> orders = sortBy.stream().map(o -> criteriaBuilder.asc(root.get(o))).toList();
                cq.orderBy(orders);
            }

            // root.fetch()

            return factory.getCurrentSession().createQuery(cq).getResultList();
*/
        } catch (HibernateException e) {
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public T save(T entity) {
        try {
            return factory.getCurrentSession().merge(entity);
        } catch (HibernateException e) {
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public void update(T entity) {
        try {
            factory.getCurrentSession().merge(entity);
        } catch (HibernateException e) {
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public void delete(PK pk) {
        try {
            if (factory
                    .getCurrentSession()
                    .createMutationQuery("DELETE FROM " + getEntityName() + " WHERE id = :id")
                    .setParameter("id", pk)
                    .executeUpdate() == 0) throw new DatabaseException("Неверный id для удаления");
        } catch (HibernateException e) {
            throw new DataManipulationException(e.getMessage());
        }
    }
}
