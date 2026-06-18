package com.metasoft.veyra.platform.communication.application.internal.queryservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.communication.domain.exceptions.ConversationNotFoundException;
import com.metasoft.veyra.platform.communication.domain.exceptions.ParticipantNotInConversationException;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.Conversation;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetConversationByIdQuery;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetConversationsByUserQuery;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetUnreadConversationCountQuery;
import com.metasoft.veyra.platform.communication.domain.services.ConversationQueryService;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.ConversationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConversationQueryServiceImpl implements ConversationQueryService {

    private final ConversationRepository conversationRepository;
    private final ExternalIamService externalIamService;

    public ConversationQueryServiceImpl(
            ConversationRepository conversationRepository,
            ExternalIamService externalIamService
    ) {
        this.conversationRepository = conversationRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Conversation> handle(GetConversationsByUserQuery query) {
        externalIamService.ensureUserExists(query.userId());
        return conversationRepository.findByParticipantUserId(query.userId());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Conversation> handle(GetConversationByIdQuery query) {
        externalIamService.ensureUserExists(query.requestingUserId());

        Conversation conversation = conversationRepository.findById(query.conversationId())
                .orElseThrow(() -> new ConversationNotFoundException(query.conversationId()));

        if (!conversation.hasParticipant(query.requestingUserId())) {
            throw new ParticipantNotInConversationException(query.requestingUserId(), query.conversationId());
        }

        return Optional.of(conversation);
    }

    @Override
    @Transactional(readOnly = true)
    public long handle(GetUnreadConversationCountQuery query) {
        externalIamService.ensureUserExists(query.userId());
        return conversationRepository.countUnreadConversationsForUser(query.userId());
    }
}
