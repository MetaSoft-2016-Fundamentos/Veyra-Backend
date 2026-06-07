package com.metasoft.veyra.platform.communication.domain.model.queries;

/**
 * Query to retrieve all conversations where the given user is a participant,
 * ordered by the most recent message.
 */
public record GetConversationsByUserQuery(Long userId) {
}
