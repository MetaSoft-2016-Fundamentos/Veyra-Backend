package com.metasoft.veyra.platform.communication.domain.model.events;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.Message;

/**
 * Domain event raised when a new chat message is successfully persisted.
 * <p>
 *     Consumed by {@code MessageBroadcastEventHandler} to push the message
 *     to WebSocket subscribers AFTER the database transaction commits,
 *     avoiding race conditions where clients query the REST API before
 *     the message is visible.
 * </p>
 */
public record MessageSentEvent(Message message) {
}
