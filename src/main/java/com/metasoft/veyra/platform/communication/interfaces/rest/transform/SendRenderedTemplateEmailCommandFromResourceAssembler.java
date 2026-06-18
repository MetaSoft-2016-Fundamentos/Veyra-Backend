package com.metasoft.veyra.platform.communication.interfaces.rest.transform;

import com.metasoft.veyra.platform.communication.domain.model.commands.SendRenderedTemplateEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.EmailRecipients;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.SendRenderedTemplateEmailResource;

public class SendRenderedTemplateEmailCommandFromResourceAssembler {

    private SendRenderedTemplateEmailCommandFromResourceAssembler() {
    }

    public static SendRenderedTemplateEmailCommand toCommandFromResource(SendRenderedTemplateEmailResource resource) {
        return new SendRenderedTemplateEmailCommand(
                new EmailRecipients(resource.recipients()),
                resource.subject(),
                resource.template(),
                resource.variables()
        );
    }
}
