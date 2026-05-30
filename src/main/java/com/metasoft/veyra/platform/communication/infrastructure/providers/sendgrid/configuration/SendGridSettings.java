package com.metasoft.veyra.platform.communication.infrastructure.providers.sendgrid.configuration;

public record SendGridSettings(
        String apiKey,
        String fromEmail,
        String fromName
) {
}
