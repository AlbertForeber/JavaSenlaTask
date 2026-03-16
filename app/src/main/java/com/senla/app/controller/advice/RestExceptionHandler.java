package com.senla.app.controller.advice;
import com.senla.app.exceptions.DataManipulationException;
import com.senla.app.exceptions.ResourceNotFound;
import com.senla.app.exceptions.UnavailableAction;
import com.senla.app.exceptions.WrongId;
import com.senla.app.model.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    // Для ошибок валидации
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {

        List<String> errors = new ArrayList<>();

        StringBuilder exceptionString;
        exception.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.add(fieldName + ": " + errorMessage);
            }
        );

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Ошибка при валидации полей",
                request.getRequestURI()
        );

        errorResponse.setDetails(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Для ошибок преобразования типов
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Type Mismatch",
                String.format("Параметр '%s' должен быть типа %s",
                    exception.getName(),
                    exception.getRequiredType()
                ),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Далее кастомные ошибки
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFound exception,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "Запрошенные объекты не найдены (" + exception.getMessage() + ")",
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongId.class)
    public ResponseEntity<ErrorResponse> handleWrongId(
            WrongId exception,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Wrong Id",
                "Неверный идентификатор сущности (" + exception.getMessage() + ")",
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataManipulationException.class)
    public ResponseEntity<ErrorResponse> handleDataManipulationException(
            DataManipulationException exception,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Data Manipulation Exception",
                exception.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnavailableAction.class)
    public ResponseEntity<ErrorResponse> handleUnavailableAction(
            UnavailableAction exception,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Unavailable Action",
                String.format("Действие '%s' недоступно", exception.getAction()),
                request.getRequestURI()
        );

        errorResponse.setDetails(List.of(exception.getReason()));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
