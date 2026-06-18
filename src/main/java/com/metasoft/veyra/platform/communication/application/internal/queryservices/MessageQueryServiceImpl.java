package com.metasoft.veyra.platform.communication.application.internal.queryservices;

import com.metasoft.veyra.platform.communication.domain.exceptions.ConversationNotFoundException;
import com.metasoft.veyra.platform.communication.domain.exceptions.ParticipantNotInConversationException;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.Conversation;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.Message;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetConversationMessagesQuery;
import com.metasoft.veyra.platform.communication.domain.services.MessageQueryService;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.ConversationRepository;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.MessageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageQueryServiceImpl implements MessageQueryService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public MessageQueryServiceImpl(
            MessageRepository messageRepository,
            ConversationRepository conversationRepository
    ) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> handle(GetConversationMessagesQuery query) {
        Conversation conversation = conversationRepository.findById(query.conversationId())
                .orElseThrow(() -> new ConversationNotFoundException(query.conversationId()));

        if (!conversation.hasParticipant(query.requestingUserId())) {
            throw new ParticipantNotInConversationException(query.requestingUserId(), query.conversationId());
        }

        // Return paginated messages ordered newest-first so the client can
        // display them in reverse (bottom of chat first), then reverse locally.
        return messageRepository
                .findByConversationIdOrderByCreatedAtDesc(
                        query.conversationId(),
                        PageRequest.of(query.page(), query.size()))
                .getContent();
    }
}
