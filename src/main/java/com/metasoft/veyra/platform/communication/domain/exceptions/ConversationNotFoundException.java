package com.metasoft.veyra.platform.communication.domain.exceptions;

public class ConversationNotFoundException extends RuntimeException {
    public ConversationNotFoundException(Long conversationId) {
        super("Conversation with ID '%d' not found.".formatted(conversationId));
    }
}
