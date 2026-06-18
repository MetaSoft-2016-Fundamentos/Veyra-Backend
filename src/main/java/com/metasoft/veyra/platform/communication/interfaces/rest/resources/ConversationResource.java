package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

import java.util.List;

public record ConversationResource(
        Long id,
        String type,
        String groupName,
        String status,
        List<Long> participantUserIds,
        String lastMessageAt,
        String createdAt
) {
}
