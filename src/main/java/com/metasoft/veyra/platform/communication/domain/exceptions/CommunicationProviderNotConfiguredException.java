package com.metasoft.veyra.platform.communication.domain.exceptions;

public class CommunicationProviderNotConfiguredException extends CommunicationIntegrationException {
    public CommunicationProviderNotConfiguredException(String providerName) {
        super("Communication provider is not configured: " + providerName);
    }
}
