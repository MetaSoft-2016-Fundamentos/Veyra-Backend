package com.metasoft.veyra.platform.communication.domain.services;

import com.metasoft.veyra.platform.communication.domain.model.commands.SendHtmlEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPlainEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendRenderedTemplateEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendTemplateEmailCommand;

public interface EmailNotificationCommandService {
    void handle(SendPlainEmailCommand command);

    void handle(SendHtmlEmailCommand command);

    void handle(SendTemplateEmailCommand command);

    void handle(SendRenderedTemplateEmailCommand command);
}
