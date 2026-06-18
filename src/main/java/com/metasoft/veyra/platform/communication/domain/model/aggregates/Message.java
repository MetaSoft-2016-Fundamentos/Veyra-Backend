package com.metasoft.veyra.platform.communication.domain.model.aggregates;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.MessageContent;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

/**
 * Message aggregate root
 * <p>
 *     Represents a single chat message sent by a user within a conversation.
 *     The message content is encapsulated in the {@link MessageContent} value object.
 * </p>
 */
@Entity
@Getter
public class Message extends AuditableAbstractAggregateRoot<Message> {

    @Column(nullable = false)
    private Long conversationId;

    @Column(nullable = false)
    private Long senderUserId;

    @Embedded
    private MessageContent content;

    protected Message() {
    }

    /**
     * Creates a new message in a conversation.
     *
     * @param conversationId the ID of the conversation this message belongs to
     * @param senderUserId   the user ID of the sender
     * @param content        the validated message content value object
     */
    public Message(Long conversationId, Long senderUserId, MessageContent content) {
        this.conversationId = conversationId;
        this.senderUserId = senderUserId;
        this.content = content;
    }
}
