package com.senla.app.task.service.unit_of_work.implementations;
import com.senla.annotation.db_qualifiers.Hibernate;
import com.senla.app.task.service.unit_of_work.AbstractUnitOfWork;
import com.senla.app.task.utils.HibernateUtil;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

@Component
@Hibernate
public class HibernateUnitOfWork extends AbstractUnitOfWork {

    private Transaction tx;

    @Override
    protected void doBegin() {
        tx = HibernateUtil.getSession().beginTransaction();
    }

    @Override
    protected void doCommit() {
        tx.commit();
        HibernateUtil.closeSession();
    }

    @Override
    protected void doRollback() {
        tx.rollback();
        HibernateUtil.closeSession();
    }
}
