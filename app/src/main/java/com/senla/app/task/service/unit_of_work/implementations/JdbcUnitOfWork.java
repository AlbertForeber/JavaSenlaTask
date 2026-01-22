package com.senla.app.task.service.unit_of_work.implementations;

import com.senla.app.task.db.DbConnection;
import com.senla.app.task.service.unit_of_work.AbstractUnitOfWork;
import com.senla.app.task.service.unit_of_work.TransactionException;

import java.sql.Connection;

public class JdbcUnitOfWork extends AbstractUnitOfWork {

    private final Connection connection = DbConnection.getInstance().initOrGetConnection();

    public JdbcUnitOfWork() { }

    @Override
    protected void doBegin() {
        try {
            connection.setAutoCommit(false);
        } catch (Exception e) {
            throw new TransactionException(e.getMessage());
        }
    }

    @Override
    protected void doCommit() {
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            throw new TransactionException(e.getMessage());
        }
    }

    @Override
    protected void doRollback() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            throw new TransactionException(e.getMessage());
        }
    }
}
