package com.senla.app.db.dao;

import com.senla.app.db.DatabaseException;
import com.senla.app.exceptions.DataManipulationException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Map;

public abstract class AbstractHibernateDao<T, PK, SB> implements GenericDao<T, PK, SB> {

    private final Class<T> type;
    private final SessionFactory factory;
    private final String sql = "SELECT " + getEntityAlias()
            + " FROM " + getEntityName() + " " + getEntityAlias();

    protected abstract String getEntityName();

    protected abstract String getEntityAlias();

    protected abstract String getAliasesForSortBy(SB sortBy);

    protected String getIdFieldName() {
        return "id";
    }

    protected String additionalJoinFetchQueryHql() {
        return "";
    }

    protected Map.Entry<String, JoinType> additionalJoinFetchQueryCriteria() {
        return Map.entry("", JoinType.INNER);
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
                findByIdSql.append(" ").append(additionalJoinFetchQueryHql());

            findByIdSql
                    .append(" WHERE ")
                    .append(getEntityAlias())
                    .append(".")
                    .append(getIdFieldName())
                    .append(" = :id");

            return factory.getCurrentSession()
                    .createQuery(findByIdSql.toString(), type)
                    .setParameter("id", pk)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public List<T> findAll(SB sortBy, boolean useJoin) {
        try {
            factory.getCurrentSession().clear();

            StringBuilder findAllSql = new StringBuilder(sql);

            if (useJoin)
                findAllSql.append(" ").append(additionalJoinFetchQueryHql());

            if (sortBy != null) {
                findAllSql
                    .append(" ORDER BY ")
                    .append(
                        String.join(", ", getAliasesForSortBy(sortBy))
                    );
            }

            return factory.getCurrentSession().createQuery(findAllSql.toString(), type).getResultList();
        } catch (Exception e) {
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public T save(T entity) {
        try {
            return factory.getCurrentSession().merge(entity);
        } catch (Exception e) {
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public void update(T entity) {
        try {
            factory.getCurrentSession().merge(entity);
        } catch (Exception e) {
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public void delete(PK pk) {
        StringBuilder hql = new StringBuilder("DELETE FROM ");
        hql
                .append(
                    getEntityAlias())
                .append("WHERE ")
                .append(getIdFieldName())
                .append(" = :id");

        try {
            if (factory
                    .getCurrentSession()
                    .createMutationQuery(hql.toString())
                    .setParameter("id", pk)
                    .executeUpdate() == 0) throw new DatabaseException("Неверный id для удаления");
        } catch (Exception e) {
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public List<T> findByField(String fieldName, Object value, boolean useJoin, boolean isJoinField) {
        // Используем Criteria API
        // В случае динамического выбора колонки фильтрации безопаснее, чем HQL

        if (isJoinField && !useJoin) {
            throw new DataManipulationException("Невозможно выполнить поиск по полю из JOIN таблицы, при выключенном useJoin");
        }

        try {
            CriteriaBuilder cb = factory.getCriteriaBuilder();
            CriteriaQuery<T> query = cb.createQuery(type);

            Root<T> root = query.from(type);
            Join<T, ?> join = null;

            if (useJoin) {
                Map.Entry<String, JoinType> joinParams = additionalJoinFetchQueryCriteria();

                root.fetch(joinParams.getKey(), joinParams.getValue());
                join = root.join(joinParams.getKey(), joinParams.getValue());
            }

            query = query.select(root).where(cb.equal(
                    isJoinField ? join.get(fieldName) : root.get(fieldName), value)
            );

            return factory.getCurrentSession().createQuery(query).getResultList();
        } catch (Exception e) {
            throw new DataManipulationException(e.getMessage());
        }
    }
}
