package com.metasoft.veyra.platform.communication.domain.exceptions;

public class ParticipantNotInConversationException extends RuntimeException {
    public ParticipantNotInConversationException(Long userId, Long conversationId) {
        super("User '%d' is not a participant of conversation '%d'.".formatted(userId, conversationId));
    }
}
