package com.senla.app.db.dao.hibernate_implementations;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.app.db.dao.AbstractHibernateDao;
import com.senla.app.model.entity.auth.RefreshToken;
import jakarta.persistence.criteria.JoinType;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Hibernate
public class HibernateRefreshTokenDao extends AbstractHibernateDao<RefreshToken, Integer, Object> {

    public HibernateRefreshTokenDao(SessionFactory factory) {
        super(RefreshToken.class, factory);
    }

    @Override
    protected String getEntityName() {
        return "refresh_tokens";
    }

    @Override
    protected String getEntityAlias() {
        return "rt";
    }

    @Override
    protected String getAliasesForSortBy(Object sortBy) {
        return "";
    }

    @Override
    protected String additionalJoinFetchQueryHql() {
        return "JOIN FETCH rt.user u";
    }

    @Override
    protected Map.Entry<String, JoinType> additionalJoinFetchQueryCriteria() {
        return Map.entry("user", JoinType.INNER);
    }
}
