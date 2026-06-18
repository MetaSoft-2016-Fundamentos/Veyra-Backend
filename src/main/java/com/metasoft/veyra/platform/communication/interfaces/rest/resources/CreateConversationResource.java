package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.ConversationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateConversationResource(
        @NotNull(message = "Participant user IDs are required")
        @Size(min = 2, message = "A conversation must have at least 2 participants")
        List<@NotNull(message = "Participant user ID must not be null") Long> participantUserIds,

        @NotNull(message = "Conversation type is required")
        ConversationType type,

        String groupName
) {
}
