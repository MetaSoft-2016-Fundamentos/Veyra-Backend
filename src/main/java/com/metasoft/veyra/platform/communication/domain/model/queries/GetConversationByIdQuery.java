package com.metasoft.veyra.platform.communication.domain.model.queries;

/**
 * Query to retrieve a single conversation by its ID.
 * <p>
 *     The requestingUserId is used to verify the user is a participant
 *     before returning the conversation data.
 * </p>
 */
public record GetConversationByIdQuery(
        Long conversationId,
        Long requestingUserId
) {
}
