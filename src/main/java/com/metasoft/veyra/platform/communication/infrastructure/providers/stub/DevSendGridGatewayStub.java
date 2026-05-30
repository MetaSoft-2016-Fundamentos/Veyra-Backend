package com.metasoft.veyra.platform.communication.infrastructure.providers.stub;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.sendgrid.SendGridGateway;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendHtmlEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPlainEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendTemplateEmailCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(name = "integrations.sendgrid.enabled", havingValue = "false")
public class DevSendGridGatewayStub implements SendGridGateway {

    @Override
    public void sendPlain(SendPlainEmailCommand command) {
        log.info("SendGrid integration disabled. Plain email not sent (recipientCount={}, subject={})",
                command.recipients().values().size(), command.subject());
    }

    @Override
    public void sendHtml(SendHtmlEmailCommand command) {
        log.info("SendGrid integration disabled. HTML email not sent (recipientCount={}, subject={})",
                command.recipients().values().size(), command.subject());
    }

    @Override
    public void sendTemplate(SendTemplateEmailCommand command) {
        log.info("SendGrid integration disabled. Template email not sent (recipientCount={}, templateId={})",
                command.recipients().values().size(), command.templateId());
    }
}
