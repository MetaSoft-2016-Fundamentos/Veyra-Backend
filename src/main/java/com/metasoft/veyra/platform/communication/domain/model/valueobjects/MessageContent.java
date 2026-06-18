package com.metasoft.veyra.platform.communication.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * MessageContent
 * <p>
 *     Value object representing the text content of a chat message.
 *     Validates that content is not blank and does not exceed 2000 characters.
 * </p>
 */
@Embeddable
public record MessageContent(String text) {

    public MessageContent {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Message content cannot be null or blank");
        }
        if (text.length() > 2000) {
            throw new IllegalArgumentException("Message content cannot exceed 2000 characters");
        }
    }
}
