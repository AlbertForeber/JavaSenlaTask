package com.senla.app.db.dao.hibernate_implementations;

import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.app.db.dao.AbstractHibernateDao;
import com.senla.app.model.entity.auth.User;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
@Hibernate
public class HibernateUserDao extends AbstractHibernateDao<User, String, Object> {

    public HibernateUserDao(SessionFactory factory) {
        super(User.class, factory);
    }

    @Override
    protected String getEntityName() {
        return "users";
    }

    @Override
    protected String getEntityAlias() {
        return "u";
    }

    @Override
    protected String getAliasesForSortBy(Object sortBy) {
        // not required
        return "";
    }

    @Override
    protected String additionalJoinFetchQuery() {
        return "JOIN FETCH u.scopes s";
    }

    @Override
    protected String getIdFieldName() {
        return "username";
    }
}
