package com.metasoft.veyra.platform.communication.application.internal.commandservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.communication.domain.exceptions.ConversationNotFoundException;
import com.metasoft.veyra.platform.communication.domain.exceptions.ParticipantNotInConversationException;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.Conversation;
import com.metasoft.veyra.platform.communication.domain.model.commands.CreateConversationCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.MarkConversationAsReadCommand;
import com.metasoft.veyra.platform.communication.domain.model.results.CreateConversationResult;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.ConversationType;
import com.metasoft.veyra.platform.communication.domain.services.ConversationCommandService;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.ConversationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConversationCommandServiceImpl implements ConversationCommandService {

    private final ConversationRepository conversationRepository;
    private final ExternalIamService externalIamService;

    public ConversationCommandServiceImpl(
            ConversationRepository conversationRepository,
            ExternalIamService externalIamService
    ) {
        this.conversationRepository = conversationRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public CreateConversationResult handle(CreateConversationCommand command) {
        // Validate all participants exist in IAM
        command.participantUserIds().forEach(externalIamService::ensureUserExists);

        if (command.type() == ConversationType.DIRECT) {
            Long userIdA = command.participantUserIds().get(0);
            Long userIdB = command.participantUserIds().get(1);

            // Return existing conversation if it already exists (idempotent)
            var existing = conversationRepository.findDirectConversationBetween(
                    ConversationType.DIRECT, userIdA, userIdB);

            if (existing.isPresent()) {
                return new CreateConversationResult(existing.get().getId(), false);
            }

            Conversation conversation = conversationRepository.save(new Conversation(userIdA, userIdB));
            return new CreateConversationResult(conversation.getId(), true);
        }

        // GROUP conversation
        Conversation conversation = conversationRepository.save(
                new Conversation(command.groupName(), command.participantUserIds()));
        return new CreateConversationResult(conversation.getId(), true);
    }

    @Override
    @Transactional
    public void handle(MarkConversationAsReadCommand command) {
        Conversation conversation = conversationRepository.findById(command.conversationId())
                .orElseThrow(() -> new ConversationNotFoundException(command.conversationId()));

        if (!conversation.hasParticipant(command.userId())) {
            throw new ParticipantNotInConversationException(command.userId(), command.conversationId());
        }

        conversation.markAsReadForUser(command.userId());
        conversationRepository.save(conversation);
    }
}
