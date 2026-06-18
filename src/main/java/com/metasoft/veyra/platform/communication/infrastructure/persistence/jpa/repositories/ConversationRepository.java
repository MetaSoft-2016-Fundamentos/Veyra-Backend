package com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.Conversation;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.ConversationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    /**
     * Returns all conversations where the given user is a participant,
     * ordered by the most recent message first.
     */
    @Query("SELECT DISTINCT c FROM Conversation c JOIN c.participants p " +
           "WHERE p.userId = :userId ORDER BY c.lastMessageAt DESC NULLS LAST")
    List<Conversation> findByParticipantUserId(@Param("userId") Long userId);

    /**
     * Finds an existing DIRECT conversation between exactly two users.
     * Used to enforce idempotency when creating direct conversations.
     */
    @Query("SELECT c FROM Conversation c JOIN c.participants p1 JOIN c.participants p2 " +
           "WHERE c.type = :type AND p1.userId = :userIdA AND p2.userId = :userIdB")
    Optional<Conversation> findDirectConversationBetween(
            @Param("type") ConversationType type,
            @Param("userIdA") Long userIdA,
            @Param("userIdB") Long userIdB);

    /**
     * Counts conversations with unread messages for a given user.
     * A conversation is considered unread when the user's lastReadAt is null
     * or earlier than the conversation's lastMessageAt.
     */
    @Query("SELECT COUNT(DISTINCT c) FROM Conversation c JOIN c.participants p " +
           "WHERE p.userId = :userId " +
           "AND c.lastMessageAt IS NOT NULL " +
           "AND (p.lastReadAt IS NULL OR p.lastReadAt < c.lastMessageAt)")
    long countUnreadConversationsForUser(@Param("userId") Long userId);
}
