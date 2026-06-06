package com.metasoft.veyra.platform.shared.interfaces.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST controllers.
 * <p>
 * This class handles exceptions thrown by REST endpoints, providing appropriate error responses.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Default constructor.
     */
    public GlobalExceptionHandler() {
        // Default constructor for Spring
    }

    /**
     * Handles MethodArgumentNotValidException.
     * @param ex the exception
     * @return an ErrorResponse with BAD_REQUEST status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleException(MethodArgumentNotValidException ex) {
        String message = ex.getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage() == null ? "" : fieldError.getDefaultMessage()).reduce("", String::concat);
        return ErrorResponse.create(
                ex,
                HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()),
                message
        );
    }

    /**
     * Handles IllegalArgumentException.
     * @param ex the exception
     * @return an ErrorResponse with BAD_REQUEST status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handleException(IllegalArgumentException ex) {
        return ErrorResponse.create(ex, HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage());
    }

}
