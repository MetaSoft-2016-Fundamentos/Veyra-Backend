package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

import java.util.List;

public record ConversationSummaryResource(
        Long id,
        String type,
        String groupName,
        List<Long> participantUserIds,
        String lastMessageAt
) {
}
