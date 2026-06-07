package com.metasoft.veyra.platform.communication.interfaces.rest.transform;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.Conversation;
import com.metasoft.veyra.platform.communication.domain.model.commands.CreateConversationCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.MarkConversationAsReadCommand;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetConversationByIdQuery;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetConversationsByUserQuery;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetUnreadConversationCountQuery;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.ConversationResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.ConversationSummaryResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.CreateConversationResource;

import java.util.List;

public final class ConversationAssembler {

    private ConversationAssembler() {
    }

    public static CreateConversationCommand toCommand(CreateConversationResource resource) {
        return new CreateConversationCommand(
                resource.participantUserIds(),
                resource.type(),
                resource.groupName()
        );
    }

    public static MarkConversationAsReadCommand toMarkAsReadCommand(Long conversationId, Long userId) {
        return new MarkConversationAsReadCommand(conversationId, userId);
    }

    public static GetConversationsByUserQuery toGetByUserQuery(Long userId) {
        return new GetConversationsByUserQuery(userId);
    }

    public static GetConversationByIdQuery toGetByIdQuery(Long conversationId, Long requestingUserId) {
        return new GetConversationByIdQuery(conversationId, requestingUserId);
    }

    public static GetUnreadConversationCountQuery toUnreadCountQuery(Long userId) {
        return new GetUnreadConversationCountQuery(userId);
    }

    public static ConversationResource toResource(Conversation conversation) {
        List<Long> participantUserIds = conversation.getParticipants().stream()
                .map(p -> p.getUserId())
                .toList();

        return new ConversationResource(
                conversation.getId(),
                conversation.getType().name(),
                conversation.getGroupName(),
                conversation.getStatus().name(),
                participantUserIds,
                conversation.getLastMessageAt() != null ? conversation.getLastMessageAt().toString() : null,
                conversation.getCreatedAt().toString()
        );
    }

    public static ConversationSummaryResource toSummaryResource(Conversation conversation) {
        List<Long> participantUserIds = conversation.getParticipants().stream()
                .map(p -> p.getUserId())
                .toList();

        return new ConversationSummaryResource(
                conversation.getId(),
                conversation.getType().name(),
                conversation.getGroupName(),
                participantUserIds,
                conversation.getLastMessageAt() != null ? conversation.getLastMessageAt().toString() : null
        );
    }
}
