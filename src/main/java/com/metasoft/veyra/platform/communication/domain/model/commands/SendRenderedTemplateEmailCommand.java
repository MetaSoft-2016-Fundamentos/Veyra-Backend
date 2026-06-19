package com.metasoft.veyra.platform.communication.domain.model.commands;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.EmailRecipients;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailTemplate;

import java.util.Map;

public record SendRenderedTemplateEmailCommand(
        EmailRecipients recipients,
        String subject,
        EmailTemplate template,
        Map<String, String> variables
) {
    public SendRenderedTemplateEmailCommand {
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Email subject cannot be null or blank");
        }
        if (template == null) {
            throw new IllegalArgumentException("Email template cannot be null");
        }
        variables = variables == null ? Map.of() : Map.copyOf(variables);
    }
}
