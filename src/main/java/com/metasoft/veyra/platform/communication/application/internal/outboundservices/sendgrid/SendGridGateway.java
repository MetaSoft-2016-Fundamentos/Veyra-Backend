package com.metasoft.veyra.platform.communication.application.internal.outboundservices.sendgrid;

import com.metasoft.veyra.platform.communication.domain.model.commands.SendHtmlEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPlainEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendTemplateEmailCommand;

public interface SendGridGateway {
    void sendPlain(SendPlainEmailCommand command);

    void sendHtml(SendHtmlEmailCommand command);

    void sendTemplate(SendTemplateEmailCommand command);
}
