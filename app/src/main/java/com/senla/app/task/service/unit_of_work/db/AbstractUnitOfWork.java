package com.senla.app.task.service.unit_of_work.db;

import com.senla.app.task.db.DbConnection;
import com.senla.app.task.service.unit_of_work.ThrowingRunnable;
import com.senla.app.task.service.unit_of_work.ThrowingSupplier;
import com.senla.app.task.service.unit_of_work.TransactionException;
import com.senla.app.task.service.unit_of_work.UnitOfWork;

import java.sql.Connection;

public abstract class AbstractUnitOfWork implements UnitOfWork {
    private boolean inTransaction = false;

    protected abstract void doBegin();
    protected abstract void doCommit();
    protected abstract void doRollback();

    private void begin() {
        try {
            doBegin();
            inTransaction = true;
        } catch (Exception e) {
            throw new TransactionException("ошибка при запуске " + e.getMessage());
        }
    }

    private void commit() {
        try {
            if (inTransaction) doCommit();
        } catch (Exception e) {
            throw new TransactionException("ошибка при подтверждении " + e.getMessage());
        } finally {
            inTransaction = false;
        }
    }

    private void rollback() {
        try {
            if (inTransaction) doRollback();
        } catch (Exception e) {
            throw new TransactionException("ошибка при откате " + e.getMessage());
        } finally {
            inTransaction = false;
        }
    }

    @Override
    public <T> T execute(ThrowingSupplier<T> operation) throws TransactionException {
        begin();
        try {
            T result = operation.get();
            commit();
            return result;
        } catch (TransactionException e) {
            rollback();
            throw e;
        } catch (Exception e) {
            rollback();
            throw new TransactionException("ошибка во время выполнения " + e.getMessage());
        }
    }

    @Override
    public void executeVoid(ThrowingRunnable operation) throws TransactionException {
        begin();
        try {
            operation.run();
            commit();
        } catch (TransactionException e) {
            rollback();
            throw e;
        } catch (Exception e) {
            rollback();
            throw new TransactionException("ошибка во время выполнения " + e.getMessage());
        }
    }

    @Override
    public void close() throws TransactionException {
        try {
            if (inTransaction) doRollback();
        } catch (Exception e) {
            throw new TransactionException("ошибка при завершении " + e.getMessage());
        }
    }
}
