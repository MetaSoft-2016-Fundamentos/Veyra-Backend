package com.metasoft.veyra.platform.communication.domain.model.queries;

/**
 * Query to retrieve the number of conversations with unread messages for a given user.
 */
public record GetUnreadConversationCountQuery(Long userId) {
}
