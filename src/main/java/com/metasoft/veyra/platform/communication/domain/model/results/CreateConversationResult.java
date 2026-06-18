package com.metasoft.veyra.platform.communication.domain.model.results;

/**
 * Result returned after attempting to create a conversation.
 * <p>
 *     isNew indicates whether a new conversation was created (true)
 *     or an existing DIRECT conversation was returned (false).
 * </p>
 */
public record CreateConversationResult(
        Long conversationId,
        boolean isNew
) {
}
