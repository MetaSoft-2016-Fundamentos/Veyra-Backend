package com.metasoft.veyra.platform.communication.interfaces.rest;

import com.metasoft.veyra.platform.communication.domain.exceptions.CommunicationIntegrationException;
import com.metasoft.veyra.platform.communication.domain.exceptions.CommunicationProviderNotConfiguredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommunicationExceptionHandler {

    @ExceptionHandler(CommunicationProviderNotConfiguredException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    ErrorResponse handleException(CommunicationProviderNotConfiguredException ex) {
        return ErrorResponse.create(ex, HttpStatusCode.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()), ex.getMessage());
    }

    @ExceptionHandler(CommunicationIntegrationException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    ErrorResponse handleException(CommunicationIntegrationException ex) {
        return ErrorResponse.create(ex, HttpStatusCode.valueOf(HttpStatus.BAD_GATEWAY.value()), ex.getMessage());
    }
}
