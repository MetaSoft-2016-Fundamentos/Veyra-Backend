package com.metasoft.veyra.platform.communication.application.internal.commandservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.communication.domain.exceptions.ConversationNotFoundException;
import com.metasoft.veyra.platform.communication.domain.exceptions.ParticipantNotInConversationException;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.Conversation;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.Message;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendMessageCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationToUserCommand;
import com.metasoft.veyra.platform.communication.domain.model.events.MessageSentEvent;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.MessageContent;
import com.metasoft.veyra.platform.communication.domain.services.MessageCommandService;
import com.metasoft.veyra.platform.communication.domain.services.PushNotificationCommandService;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.ConversationRepository;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.MessageRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageCommandServiceImpl implements MessageCommandService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final ExternalIamService externalIamService;
    private final PushNotificationCommandService pushNotificationCommandService;
    private final ApplicationEventPublisher eventPublisher;

    public MessageCommandServiceImpl(
            MessageRepository messageRepository,
            ConversationRepository conversationRepository,
            ExternalIamService externalIamService,
            PushNotificationCommandService pushNotificationCommandService,
            ApplicationEventPublisher eventPublisher
    ) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.externalIamService = externalIamService;
        this.pushNotificationCommandService = pushNotificationCommandService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Message handle(SendMessageCommand command) {
        externalIamService.ensureUserExists(command.senderUserId());

        Conversation conversation = conversationRepository.findById(command.conversationId())
                .orElseThrow(() -> new ConversationNotFoundException(command.conversationId()));

        if (!conversation.hasParticipant(command.senderUserId())) {
            throw new ParticipantNotInConversationException(command.senderUserId(), command.conversationId());
        }

        Message message = messageRepository.save(
                new Message(command.conversationId(), command.senderUserId(), new MessageContent(command.content())));

        conversation.updateLastMessageAt();
        conversationRepository.save(conversation);

        // Publish domain event — broadcast happens AFTER_COMMIT via MessageBroadcastEventHandler
        eventPublisher.publishEvent(new MessageSentEvent(message));

        // Send push notification to all other participants (best-effort, non-blocking)
        conversation.getParticipants().stream()
                .filter(p -> !p.getUserId().equals(command.senderUserId()))
                .forEach(p -> {
                    try {
                        pushNotificationCommandService.handle(
                                new SendPushNotificationToUserCommand(
                                        p.getUserId(),
                                        "Nuevo mensaje",
                                        command.content().length() > 100
                                                ? command.content().substring(0, 97) + "..."
                                                : command.content()
                                )
                        );
                    } catch (Exception ignored) {
                        // Push notification failure must not roll back the message
                    }
                });

        return message;
    }
}
