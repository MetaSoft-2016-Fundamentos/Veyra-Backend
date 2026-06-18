package com.metasoft.veyra.platform.communication.interfaces.rest.transform;

import com.metasoft.veyra.platform.communication.domain.model.commands.SendHtmlEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.EmailRecipients;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.SendHtmlEmailResource;

public class SendHtmlEmailCommandFromResourceAssembler {

    private SendHtmlEmailCommandFromResourceAssembler() {
    }

    public static SendHtmlEmailCommand toCommandFromResource(SendHtmlEmailResource resource) {
        return new SendHtmlEmailCommand(
                new EmailRecipients(resource.recipients()),
                resource.subject(),
                resource.htmlContent(),
                resource.plainContent()
        );
    }
}
