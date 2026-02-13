package com.senla.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongId extends RuntimeException {

    public WrongId(String message) {
        super(message);
    }
}
