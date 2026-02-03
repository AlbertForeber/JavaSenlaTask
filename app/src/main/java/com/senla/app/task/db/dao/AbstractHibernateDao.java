package com.senla.app.task.db.dao;

import com.senla.app.task.db.DatabaseException;
import com.senla.app.task.utils.HibernateUtil;
import jakarta.persistence.NoResultException;
import org.hibernate.HibernateException;

import java.util.List;

public abstract class AbstractHibernateDao<T, PK, SB> implements GenericDao<T, PK, SB> {

    private final Class<T> type;
    private final HibernateUtil hibernateUtil;
    private final String sql = "SELECT " + getEntityAlias()
            + " FROM " + getEntityName() + " " + getEntityAlias();

    protected abstract String getEntityName();

    protected abstract String getEntityAlias();

    protected abstract String getAliasesForSortBy(SB sortBy);

    protected String additionalJoinFetchQuery() {
        return "";
    }

    public AbstractHibernateDao(Class<T> type, HibernateUtil hibernateUtil) {
        this.type = type;
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public T findById(PK pk, boolean useJoin) {
        try {
            StringBuilder findByIdSql = new StringBuilder(sql);

            if (useJoin)
                findByIdSql.append(" ").append(additionalJoinFetchQuery());
            findByIdSql.append(" WHERE ").append(getEntityAlias()).append(".id = :id");

            return hibernateUtil.getSession().createQuery(findByIdSql.toString(), type).setParameter("id", pk).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public List<T> findAll(SB sortBy, boolean useJoin) {
        try {
            hibernateUtil.getSession().clear();

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

            return hibernateUtil.getSession().createQuery(findAllSql.toString(), type).getResultList();
/*
            CriteriaBuilder criteriaBuilder = hibernateUtil.getSession().getCriteriaBuilder();
            CriteriaQuery<T> cq = criteriaBuilder.createQuery(type);
            Root<T> root = cq.from(type);

            if (sortBy != null) {
                List<Order> orders = sortBy.stream().map(o -> criteriaBuilder.asc(root.get(o))).toList();
                cq.orderBy(orders);
            }

            // root.fetch()

            return hibernateUtil.getSession().createQuery(cq).getResultList();
*/
        } catch (HibernateException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public void save(T entity) {
        try {
            hibernateUtil.getSession().merge(entity);
        } catch (HibernateException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public void update(T entity) {
        try {
            hibernateUtil.getSession().merge(entity);
        } catch (HibernateException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public void delete(PK pk) {
        try {
            if (hibernateUtil
                    .getSession()
                    .createMutationQuery("DELETE FROM " + getEntityName() + " WHERE id = :id")
                    .setParameter("id", pk)
                    .executeUpdate() == 0) throw new DatabaseException("Неверный id для удаления");
        } catch (HibernateException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
