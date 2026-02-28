package com.senla.db;

import com.senla.entity.MoneyTransfer;
import jakarta.persistence.NoResultException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("transfer")
public class TransferDao implements SimpleGenericDao<MoneyTransfer> {

    private final SessionFactory sessionFactory;

    public TransferDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public MoneyTransfer findById(Integer id) {
        try {
            return sessionFactory.getCurrentSession().createQuery(
                    "SELECT m FROM money_transfers m WHERE m.id = :id", MoneyTransfer.class
            ).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public MoneyTransfer save(MoneyTransfer entity) {
        return sessionFactory.getCurrentSession().merge(entity);
    }
}
