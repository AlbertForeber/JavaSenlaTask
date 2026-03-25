package com.senla.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DataManipulationException extends RuntimeException {

  public DataManipulationException(String message) {
    super(message);
  }
}
