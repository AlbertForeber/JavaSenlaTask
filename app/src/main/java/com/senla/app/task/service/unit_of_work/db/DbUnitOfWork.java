package com.senla.app.task.service.unit_of_work.db;

import com.senla.app.task.db.DbConnection;
import com.senla.app.task.service.unit_of_work.ThrowingRunnable;
import com.senla.app.task.service.unit_of_work.ThrowingSupplier;
import com.senla.app.task.service.unit_of_work.UnitOfWork;

import java.sql.Connection;
import java.sql.SQLException;

public class DbUnitOfWork implements UnitOfWork {
    Connection connection = DbConnection.getInstance().initOrGetConnection();

    @Override
    public void begin() {
        try {
            connection.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка начала транзакции");
        }
    }

    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка подтверждения транзакции");
        }
    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка отката транзакции");
        }
    }

    @Override
    public <T, E extends Exception> T execute(ThrowingSupplier<T, E> operation) throws E {
        begin();
        try {
            T result = operation.get();
            commit();
        } catch (Exception e) {
            rollback();
            throw new RuntimeException(e);
        }
    }

    @Override
    public <E extends Exception> void executeVoid(ThrowingRunnable<E> operation) throws E {

    }

    @Override
    public void close() throws Exception {

    }
}
