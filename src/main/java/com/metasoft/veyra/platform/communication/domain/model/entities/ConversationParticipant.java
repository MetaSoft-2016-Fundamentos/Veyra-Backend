package com.metasoft.veyra.platform.communication.domain.model.entities;

import com.metasoft.veyra.platform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * ConversationParticipant entity
 * <p>
 *     Represents a user participating in a conversation.
 *     Tracks when the user joined and when they last read the conversation.
 * </p>
 */
@Entity
@Getter
public class ConversationParticipant extends AuditableModel {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    @Column
    private LocalDateTime lastReadAt;

    protected ConversationParticipant() {
    }

    public ConversationParticipant(Long userId) {
        this.userId = userId;
        this.joinedAt = LocalDateTime.now();
    }

    /**
     * Marks the conversation as read for this participant at the current timestamp.
     */
    public void markAsRead() {
        this.lastReadAt = LocalDateTime.now();
    }
}
