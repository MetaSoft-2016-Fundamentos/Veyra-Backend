package com.metasoft.veyra.platform.communication.domain.model.commands;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.EmailRecipients;

import java.util.Map;

public record SendTemplateEmailCommand(
        EmailRecipients recipients,
        String templateId,
        Map<String, Object> dynamicTemplateData
) {
    public SendTemplateEmailCommand {
        if (templateId == null || templateId.isBlank()) {
            throw new IllegalArgumentException("SendGrid template id cannot be null or blank");
        }
        dynamicTemplateData = dynamicTemplateData == null ? Map.of() : Map.copyOf(dynamicTemplateData);
    }
}
