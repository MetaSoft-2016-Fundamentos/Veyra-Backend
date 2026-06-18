package com.metasoft.veyra.platform.communication.domain.exceptions;

public class SendGridIntegrationException extends CommunicationIntegrationException {
    public SendGridIntegrationException(String message) {
        super(message);
    }

    public SendGridIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
