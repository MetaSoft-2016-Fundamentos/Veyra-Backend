package com.metasoft.veyra.platform.communication.domain.model.commands;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.EmailRecipients;

public record SendPlainEmailCommand(
        EmailRecipients recipients,
        String subject,
        String plainContent
) {
    public SendPlainEmailCommand {
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Email subject cannot be null or blank");
        }
        if (plainContent == null || plainContent.isBlank()) {
            throw new IllegalArgumentException("Plain email content cannot be null or blank");
        }
    }
}
