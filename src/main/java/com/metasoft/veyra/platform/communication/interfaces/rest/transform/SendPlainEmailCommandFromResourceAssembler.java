package com.metasoft.veyra.platform.communication.interfaces.rest.transform;

import com.metasoft.veyra.platform.communication.domain.model.commands.SendPlainEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.EmailRecipients;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.SendPlainEmailResource;

public class SendPlainEmailCommandFromResourceAssembler {

    private SendPlainEmailCommandFromResourceAssembler() {
    }

    public static SendPlainEmailCommand toCommandFromResource(SendPlainEmailResource resource) {
        return new SendPlainEmailCommand(
                new EmailRecipients(resource.recipients()),
                resource.subject(),
                resource.plainContent()
        );
    }
}
