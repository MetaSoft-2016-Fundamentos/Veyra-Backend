package com.metasoft.veyra.platform.communication.interfaces.rest.transform;

import com.metasoft.veyra.platform.communication.domain.model.commands.SendTemplateEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.EmailRecipients;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.SendTemplateEmailResource;

public class SendTemplateEmailCommandFromResourceAssembler {

    private SendTemplateEmailCommandFromResourceAssembler() {
    }

    public static SendTemplateEmailCommand toCommandFromResource(SendTemplateEmailResource resource) {
        return new SendTemplateEmailCommand(
                new EmailRecipients(resource.recipients()),
                resource.templateId(),
                resource.dynamicTemplateData()
        );
    }
}
