package com.metasoft.veyra.platform.communication.domain.exceptions;

public class CommunicationIntegrationException extends RuntimeException {
    public CommunicationIntegrationException(String message) {
        super(message);
    }

    public CommunicationIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
