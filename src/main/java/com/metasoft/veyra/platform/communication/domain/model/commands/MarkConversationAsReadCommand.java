package com.metasoft.veyra.platform.communication.domain.model.commands;

/**
 * Command to mark all messages in a conversation as read for a given user.
 */
public record MarkConversationAsReadCommand(
        Long conversationId,
        Long userId
) {
    public MarkConversationAsReadCommand {
        if (conversationId == null) {
            throw new IllegalArgumentException("Conversation ID cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }
}
