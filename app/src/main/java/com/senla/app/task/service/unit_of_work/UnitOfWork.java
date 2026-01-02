package com.senla.app.task.service.unit_of_work;

import java.sql.SQLException;

public interface UnitOfWork extends AutoCloseable {
    void begin();
    void commit();
    void rollback();

    // Для возвращающих функций
    <T, E extends Exception> T execute(ThrowingSupplier<T, E> operation) throws E;

    // Для ничего не возвращающих функций
    <E extends Exception> void executeVoid(ThrowingRunnable<E> operation) throws E;
}

