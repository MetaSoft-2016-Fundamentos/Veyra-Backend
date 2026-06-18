package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

public record ChatMessageResource(
        Long id,
        Long conversationId,
        Long senderUserId,
        String content,
        String createdAt
) {
}
