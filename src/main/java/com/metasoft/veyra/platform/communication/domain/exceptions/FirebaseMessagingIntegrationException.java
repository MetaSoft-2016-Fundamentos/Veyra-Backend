package com.metasoft.veyra.platform.communication.domain.exceptions;

public class FirebaseMessagingIntegrationException extends CommunicationIntegrationException {
    public FirebaseMessagingIntegrationException(String message) {
        super(message);
    }

    public FirebaseMessagingIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
