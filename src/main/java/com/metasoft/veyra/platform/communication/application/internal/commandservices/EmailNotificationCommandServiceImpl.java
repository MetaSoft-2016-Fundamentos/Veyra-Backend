package com.metasoft.veyra.platform.communication.application.internal.commandservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.sendgrid.SendGridGateway;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendHtmlEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPlainEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendTemplateEmailCommand;
import com.metasoft.veyra.platform.communication.domain.services.EmailNotificationCommandService;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationCommandServiceImpl implements EmailNotificationCommandService {

    private final SendGridGateway sendGridGateway;

    public EmailNotificationCommandServiceImpl(SendGridGateway sendGridGateway) {
        this.sendGridGateway = sendGridGateway;
    }

    @Override
    public void handle(SendPlainEmailCommand command) {
        sendGridGateway.sendPlain(command);
    }

    @Override
    public void handle(SendHtmlEmailCommand command) {
        sendGridGateway.sendHtml(command);
    }

    @Override
    public void handle(SendTemplateEmailCommand command) {
        sendGridGateway.sendTemplate(command);
    }
}
