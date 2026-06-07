package com.metasoft.veyra.platform.communication.domain.exceptions;

public class DirectConversationParticipantLimitException extends RuntimeException {
    public DirectConversationParticipantLimitException() {
        super("A direct conversation must have exactly 2 participants.");
    }
}
