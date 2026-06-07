package com.metasoft.veyra.platform.communication.domain.model.commands;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.ConversationType;

import java.util.List;

/**
 * Command to create a new conversation.
 * <p>
 *     For DIRECT conversations, participantUserIds must contain exactly 2 user IDs.
 *     For GROUP conversations, participantUserIds must contain at least 2 user IDs
 *     and groupName must be provided.
 * </p>
 */
public record CreateConversationCommand(
        List<Long> participantUserIds,
        ConversationType type,
        String groupName
) {
    public CreateConversationCommand {
        if (participantUserIds == null || participantUserIds.size() < 2) {
            throw new IllegalArgumentException("A conversation requires at least 2 participants");
        }
        if (type == ConversationType.DIRECT && participantUserIds.size() != 2) {
            throw new IllegalArgumentException("A direct conversation must have exactly 2 participants");
        }
        if (type == ConversationType.GROUP && (groupName == null || groupName.isBlank())) {
            throw new IllegalArgumentException("Group name is required for group conversations");
        }
    }
}
