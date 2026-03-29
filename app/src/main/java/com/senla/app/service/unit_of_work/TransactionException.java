package com.senla.app.service.unit_of_work;

public class TransactionException extends RuntimeException {

    public TransactionException(String message) {
        super(message);
    }
}
