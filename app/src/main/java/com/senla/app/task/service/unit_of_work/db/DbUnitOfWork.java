package com.senla.app.task.service.unit_of_work.db;

import com.senla.app.task.db.DbConnection;

import java.sql.Connection;

public class DbUnitOfWork extends AbstractUnitOfWork {

    private final Connection connection = DbConnection.getInstance().initOrGetConnection();

    public DbUnitOfWork() { }

    @Override
    protected void doBegin() {
        try {
            connection.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void doCommit() {
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void doRollback() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
