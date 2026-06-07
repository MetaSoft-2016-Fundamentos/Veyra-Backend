package com.metasoft.veyra.platform.communication.application.internal.eventhandlers;

import com.metasoft.veyra.platform.communication.domain.model.events.MessageSentEvent;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.ChatMessageResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.transform.ChatMessageAssembler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Broadcasts a new chat message to WebSocket subscribers after the transaction commits.
 * <p>
 *     Using {@link TransactionalEventListener} with {@code AFTER_COMMIT} ensures that
 *     the message is visible in the database before any subscriber queries the REST API
 *     in response to the WebSocket notification.
 * </p>
 */
@Component
public class MessageBroadcastEventHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public MessageBroadcastEventHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(MessageSentEvent event) {
        ChatMessageResource payload = ChatMessageAssembler.toResource(event.message());
        messagingTemplate.convertAndSend(
                "/topic/conversations/" + event.message().getConversationId(),
                payload
        );
    }
}
