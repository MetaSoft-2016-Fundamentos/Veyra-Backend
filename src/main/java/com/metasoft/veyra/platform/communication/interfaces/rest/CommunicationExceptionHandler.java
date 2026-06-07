package com.metasoft.veyra.platform.communication.interfaces.rest;

import com.metasoft.veyra.platform.communication.domain.exceptions.CommunicationIntegrationException;
import com.metasoft.veyra.platform.communication.domain.exceptions.CommunicationProviderNotConfiguredException;
import com.metasoft.veyra.platform.communication.domain.exceptions.ConversationNotFoundException;
import com.metasoft.veyra.platform.communication.domain.exceptions.DuplicateDirectConversationException;
import com.metasoft.veyra.platform.communication.domain.exceptions.ParticipantNotInConversationException;
import com.metasoft.veyra.platform.communication.domain.exceptions.UserNotFoundForPushException;
import com.metasoft.veyra.platform.communication.domain.exceptions.UserNotificationNotFoundException;
import com.metasoft.veyra.platform.communication.domain.exceptions.UserPushTokenNotFoundException;
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

    @ExceptionHandler({UserNotFoundForPushException.class, UserPushTokenNotFoundException.class, UserNotificationNotFoundException.class, ConversationNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorResponse handleNotFoundException(RuntimeException ex) {
        return ErrorResponse.create(ex, HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()), ex.getMessage());
    }

    @ExceptionHandler(ParticipantNotInConversationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ErrorResponse handleParticipantNotInConversation(ParticipantNotInConversationException ex) {
        return ErrorResponse.create(ex, HttpStatusCode.valueOf(HttpStatus.FORBIDDEN.value()), ex.getMessage());
    }

    @ExceptionHandler(DuplicateDirectConversationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    ErrorResponse handleDuplicateDirectConversation(DuplicateDirectConversationException ex) {
        return ErrorResponse.create(ex, HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()), ex.getMessage());
    }
}
