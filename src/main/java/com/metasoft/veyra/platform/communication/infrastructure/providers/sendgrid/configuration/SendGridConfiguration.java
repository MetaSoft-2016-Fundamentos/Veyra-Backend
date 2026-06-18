package com.metasoft.veyra.platform.communication.infrastructure.providers.sendgrid.configuration;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfiguration {

    @Value("${sendgrid.api.key:}")
    private String apiKey;

    @Value("${sendgrid.from.email:}")
    private String fromEmail;

    @Value("${sendgrid.from.name:}")
    private String fromName;

    @Bean
    SendGridSettings sendGridSettings() {
        return new SendGridSettings(apiKey, fromEmail, fromName);
    }

    @Bean
    @ConditionalOnProperty(name = "integrations.sendgrid.enabled", havingValue = "true")
    SendGrid sendGrid(SendGridSettings sendGridSettings) {
        return new SendGrid(sendGridSettings.apiKey());
    }
}
