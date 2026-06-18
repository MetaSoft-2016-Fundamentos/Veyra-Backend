package com.metasoft.veyra.platform.communication.domain.model.valueobjects;

/**
 * ConversationType
 * <p>
 *     Defines the type of a conversation.
 *     DIRECT: a private conversation between exactly two users.
 *     GROUP: a conversation with two or more participants and an optional group name.
 * </p>
 */
public enum ConversationType {
    /** A private one-on-one conversation. */
    DIRECT,
    /** A group conversation with multiple participants. */
    GROUP
}
