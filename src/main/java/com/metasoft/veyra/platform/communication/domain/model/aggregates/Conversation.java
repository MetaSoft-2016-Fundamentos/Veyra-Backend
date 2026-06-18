package com.metasoft.veyra.platform.communication.domain.model.aggregates;

import com.metasoft.veyra.platform.communication.domain.model.entities.ConversationParticipant;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.ConversationStatus;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.ConversationType;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Conversation aggregate root
 * <p>
 *     Represents a chat conversation between two or more users.
 *     Supports DIRECT (one-on-one) and GROUP conversations.
 *     Owns the set of ConversationParticipant entities.
 * </p>
 */
@Entity
@Getter
public class Conversation extends AuditableAbstractAggregateRoot<Conversation> {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConversationType type;

    @Column(length = 100)
    private String groupName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConversationStatus status;

    @Column
    private LocalDateTime lastMessageAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "conversation_id")
    private Set<ConversationParticipant> participants = new HashSet<>();

    protected Conversation() {
    }

    /**
     * Creates a DIRECT conversation between exactly two users.
     *
     * @param userIdA the first participant's user ID
     * @param userIdB the second participant's user ID
     */
    public Conversation(Long userIdA, Long userIdB) {
        this.type = ConversationType.DIRECT;
        this.status = ConversationStatus.ACTIVE;
        this.participants.add(new ConversationParticipant(userIdA));
        this.participants.add(new ConversationParticipant(userIdB));
    }

    /**
     * Creates a GROUP conversation with a name and a list of participant user IDs.
     *
     * @param groupName the display name of the group
     * @param userIds   the list of participant user IDs (minimum 2)
     */
    public Conversation(String groupName, List<Long> userIds) {
        this.type = ConversationType.GROUP;
        this.groupName = groupName;
        this.status = ConversationStatus.ACTIVE;
        userIds.forEach(uid -> this.participants.add(new ConversationParticipant(uid)));
    }

    /**
     * Checks whether the given user is a participant in this conversation.
     *
     * @param userId the user ID to check
     * @return true if the user is a participant, false otherwise
     */
    public boolean hasParticipant(Long userId) {
        return participants.stream().anyMatch(p -> p.getUserId().equals(userId));
    }

    /**
     * Adds a new participant to this conversation if not already present.
     * Only valid for GROUP conversations.
     *
     * @param userId the user ID of the new participant
     */
    public void addParticipant(Long userId) {
        if (!hasParticipant(userId)) {
            participants.add(new ConversationParticipant(userId));
        }
    }

    /**
     * Updates the lastMessageAt timestamp to the current time.
     * Called each time a new message is sent in this conversation.
     */
    public void updateLastMessageAt() {
        this.lastMessageAt = LocalDateTime.now();
    }

    /**
     * Marks the conversation as read for the given user.
     *
     * @param userId the user ID marking the conversation as read
     */
    public void markAsReadForUser(Long userId) {
        participants.stream()
                .filter(p -> p.getUserId().equals(userId))
                .findFirst()
                .ifPresent(ConversationParticipant::markAsRead);
    }
}
