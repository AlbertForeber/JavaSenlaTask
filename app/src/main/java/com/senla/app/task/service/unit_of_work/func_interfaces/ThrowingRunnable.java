package com.senla.app.task.service.unit_of_work.func_interfaces;

@FunctionalInterface
public interface ThrowingRunnable {

    void run() throws Exception;
}
