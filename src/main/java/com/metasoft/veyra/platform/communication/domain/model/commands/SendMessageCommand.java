package com.metasoft.veyra.platform.communication.domain.model.commands;

/**
 * Command to send a message within an existing conversation.
 */
public record SendMessageCommand(
        Long conversationId,
        Long senderUserId,
        String content
) {
    public SendMessageCommand {
        if (conversationId == null) {
            throw new IllegalArgumentException("Conversation ID cannot be null");
        }
        if (senderUserId == null) {
            throw new IllegalArgumentException("Sender user ID cannot be null");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Message content cannot be null or blank");
        }
        if (content.length() > 2000) {
            throw new IllegalArgumentException("Message content cannot exceed 2000 characters");
        }
    }
}
