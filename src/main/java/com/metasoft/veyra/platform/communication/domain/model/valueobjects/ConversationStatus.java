package com.metasoft.veyra.platform.communication.domain.model.valueobjects;

/**
 * ConversationStatus
 * <p>
 *     Defines the lifecycle status of a conversation.
 *     ACTIVE: the conversation is open and participants can send messages.
 *     ARCHIVED: the conversation is closed and no new messages are accepted.
 * </p>
 */
public enum ConversationStatus {
    /** The conversation is active and open for messages. */
    ACTIVE,
    /** The conversation has been archived and is read-only. */
    ARCHIVED
}
