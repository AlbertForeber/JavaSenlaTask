package com.senla.app.task.service.unit_of_work;

@FunctionalInterface
public interface ThrowingRunnable {
    void run() throws Exception;
}
