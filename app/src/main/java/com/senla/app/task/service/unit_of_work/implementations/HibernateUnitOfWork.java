package com.senla.app.task.service.unit_of_work.implementations;
import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.app.task.service.unit_of_work.AbstractUnitOfWork;
import com.senla.app.task.utils.HibernateUtil;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

@Component
@Hibernate
public class HibernateUnitOfWork extends AbstractUnitOfWork {

    private final HibernateUtil hibernateUtil;
    private Transaction tx;

    public HibernateUnitOfWork(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    protected void doBegin() {
        tx = hibernateUtil.getSession().beginTransaction();
    }

    @Override
    protected void doCommit() {
        tx.commit();
        hibernateUtil.closeSession();
    }

    @Override
    protected void doRollback() {
        tx.rollback();
        hibernateUtil.closeSession();
    }
}
