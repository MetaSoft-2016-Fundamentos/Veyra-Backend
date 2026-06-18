package com.metasoft.veyra.platform.communication.domain.model.commands;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.EmailRecipients;

public record SendHtmlEmailCommand(
        EmailRecipients recipients,
        String subject,
        String htmlContent,
        String plainContent
) {
    public SendHtmlEmailCommand {
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Email subject cannot be null or blank");
        }
        if (htmlContent == null || htmlContent.isBlank()) {
            throw new IllegalArgumentException("HTML email content cannot be null or blank");
        }
    }
}
