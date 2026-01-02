package com.senla.app.task.service.unit_of_work;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends  Exception> {
    T get() throws E;
}