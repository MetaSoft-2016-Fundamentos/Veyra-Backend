package com.metasoft.veyra.platform.communication.domain.model.valueobjects;

import java.util.List;

public record EmailRecipients(List<String> values) {
    public EmailRecipients {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Email recipients cannot be null or empty");
        }
        List<String> normalizedRecipients = values.stream()
                .filter(recipient -> recipient != null && !recipient.isBlank())
                .map(String::trim)
                .toList();
        if (normalizedRecipients.isEmpty()) {
            throw new IllegalArgumentException("Email recipients cannot be null or empty");
        }
        values = List.copyOf(normalizedRecipients);
    }
}
