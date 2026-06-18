package com.metasoft.veyra.platform.communication.domain.model.queries;

/**
 * Query to retrieve the messages of a conversation in ascending chronological order.
 * <p>
 *     The requestingUserId is used to verify the user is a participant
 *     before returning the messages.
 *     Supports pagination via page and size parameters.
 * </p>
 */
public record GetConversationMessagesQuery(
        Long conversationId,
        Long requestingUserId,
        int page,
        int size
) {
}
