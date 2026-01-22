package com.senla.app.task.service.unit_of_work.func_interfaces;

@FunctionalInterface
public interface ThrowingSupplier<T> {

    T get() throws Exception;
}