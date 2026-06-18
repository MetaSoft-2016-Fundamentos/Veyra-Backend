package com.metasoft.veyra.platform.communication.interfaces.rest.transform;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.Message;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendMessageCommand;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetConversationMessagesQuery;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.ChatMessageResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.SendMessageResource;

public final class ChatMessageAssembler {

    private ChatMessageAssembler() {
    }

    public static SendMessageCommand toCommand(Long conversationId, SendMessageResource resource) {
        return new SendMessageCommand(conversationId, resource.senderUserId(), resource.content());
    }

    public static GetConversationMessagesQuery toGetMessagesQuery(
            Long conversationId, Long requestingUserId, int page, int size) {
        return new GetConversationMessagesQuery(conversationId, requestingUserId, page, size);
    }

    public static ChatMessageResource toResource(Message message) {
        return new ChatMessageResource(
                message.getId(),
                message.getConversationId(),
                message.getSenderUserId(),
                message.getContent().text(),
                message.getCreatedAt().toString()
        );
    }
}
