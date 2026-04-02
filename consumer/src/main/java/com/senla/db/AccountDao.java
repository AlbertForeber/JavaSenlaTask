package com.senla.db;

import com.senla.entity.Account;
import jakarta.persistence.NoResultException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("account")
public class AccountDao implements SimpleGenericDao<Account> {

    private final SessionFactory sessionFactory;

    public AccountDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Account findById(Integer id) {
        try {
            return sessionFactory.getCurrentSession().createQuery(
                    "SELECT a FROM accounts a WHERE a.id = :id", Account.class
            ).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Account save(Account entity) {
        return sessionFactory.getCurrentSession().merge(entity);
    }
}
