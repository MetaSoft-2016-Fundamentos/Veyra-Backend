package com.metasoft.veyra.platform.communication.domain.exceptions;

public class DuplicateDirectConversationException extends RuntimeException {
    public DuplicateDirectConversationException(Long userIdA, Long userIdB) {
        super("A direct conversation between users '%d' and '%d' already exists.".formatted(userIdA, userIdB));
    }
}
