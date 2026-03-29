package com.senla.app.service.unit_of_work;

import com.senla.app.service.unit_of_work.func_interfaces.ThrowingRunnable;
import com.senla.app.service.unit_of_work.func_interfaces.ThrowingSupplier;

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
            throw new TransactionException("ОШИБКА ДОСТУПА К БАЗЕ: Не удалось запустить транзакцию (" + e.getMessage() + ")");
        }
    }

    private void commit() {
        try {
            if (inTransaction) doCommit();
        } catch (Exception e) {
            throw new TransactionException("ОШИБКА ДОСТУПА К БАЗЕ: Не удалось завершить транзакцию (" + e.getMessage() + ")");
        } finally {
            inTransaction = false;
        }
    }

    private void rollback() {
        try {
            if (inTransaction) doRollback();
        } catch (Exception e) {
            throw new TransactionException("ОШИБКА ДОСТУПА К БАЗЕ: Не удалось отменить транзакцию (" + e.getMessage() + ")");
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
            throw new TransactionException("ОШИБКА ДОСТУПА К БАЗЕ: При выполнении транзакции произошла ошибка (" + e.getMessage() + ")");
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
            throw new TransactionException("ОШИБКА ДОСТУПА К БАЗЕ: При выполнении транзакции произошла ошибка (" + e.getMessage() + ")" + e.getMessage());
        }
    }

    @Override
    public void close() throws TransactionException {
        try {
            if (inTransaction) doRollback();
        } catch (Exception e) {
            throw new TransactionException("ОШИБКА ДОСТУПА К БАЗЕ: Не удалось отменить транзакцию (" + e.getMessage() + ")");
        }
    }
}
