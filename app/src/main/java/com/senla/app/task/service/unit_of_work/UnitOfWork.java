package com.senla.app.task.service.unit_of_work;

import java.sql.SQLException;

public interface UnitOfWork extends AutoCloseable {
    // Для возвращающих функций
    <T> T execute(ThrowingSupplier<T> operation) throws TransactionException;

    // Для ничего не возвращающих функций
    void executeVoid(ThrowingRunnable operation) throws TransactionException;
}

