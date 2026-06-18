package com.metasoft.veyra.platform.communication.infrastructure.providers.sendgrid.services;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.sendgrid.SendGridGateway;
import com.metasoft.veyra.platform.communication.domain.exceptions.CommunicationProviderNotConfiguredException;
import com.metasoft.veyra.platform.communication.domain.exceptions.SendGridIntegrationException;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendHtmlEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPlainEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendTemplateEmailCommand;
import com.metasoft.veyra.platform.communication.infrastructure.providers.sendgrid.configuration.SendGridSettings;
import com.metasoft.veyra.platform.communication.infrastructure.providers.sendgrid.SendGridMailBuilder;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@ConditionalOnProperty(name = "integrations.sendgrid.enabled", havingValue = "true")
public class SendGridGatewayImpl implements SendGridGateway {

    private final SendGridSettings sendGridSettings;
    private final SendGrid sendGrid;

    public SendGridGatewayImpl(SendGridSettings sendGridSettings, SendGrid sendGrid) {
        this.sendGridSettings = sendGridSettings;
        this.sendGrid = sendGrid;
    }

    @Override
    public void sendPlain(SendPlainEmailCommand command) {
        validateConfiguration();
        Mail mail = SendGridMailBuilder.buildPlainMail(sendGridSettings, command);
        sendMail(mail, "plain", command.recipients().values().size(), command.subject());
    }

    @Override
    public void sendHtml(SendHtmlEmailCommand command) {
        validateConfiguration();
        Mail mail = SendGridMailBuilder.buildHtmlMail(sendGridSettings, command);
        sendMail(mail, "html", command.recipients().values().size(), command.subject());
    }

    @Override
    public void sendTemplate(SendTemplateEmailCommand command) {
        validateConfiguration();
        Mail mail = SendGridMailBuilder.buildTemplateMail(sendGridSettings, command);
        sendMail(mail, "template", command.recipients().values().size(), command.templateId());
    }

    private void validateConfiguration() {
        if (sendGridSettings.apiKey() == null || sendGridSettings.apiKey().isBlank()) {
            throw new CommunicationProviderNotConfiguredException("SendGrid");
        }
        if (sendGridSettings.fromEmail() == null || sendGridSettings.fromEmail().isBlank()) {
            throw new CommunicationProviderNotConfiguredException("SendGrid");
        }
    }

    private void sendMail(Mail mail, String emailType, int recipientCount, String reference) {
        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);
            int statusCode = response.getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                log.info("SendGrid email accepted (type={}, recipientCount={}, reference={}, statusCode={})",
                        emailType, recipientCount, reference, statusCode);
                return;
            }

            log.error("SendGrid email rejected (type={}, recipientCount={}, reference={}, statusCode={})",
                    emailType, recipientCount, reference, statusCode);
            throw new SendGridIntegrationException("SendGrid rejected email request with status " + statusCode);
        } catch (IOException exception) {
            log.error("SendGrid email failed (type={}, recipientCount={}, reference={})",
                    emailType, recipientCount, reference);
            throw new SendGridIntegrationException("Failed to send email via SendGrid", exception);
        }
    }
}
