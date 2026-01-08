package com.senla.app.task.service.unit_of_work;

public class TransactionException extends RuntimeException {

    public TransactionException(String message) {
        super("ОШИБКА ТРАНЗАКЦИИ: " + message);
    }
}
