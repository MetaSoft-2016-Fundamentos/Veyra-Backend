package com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Returns all messages in a conversation ordered chronologically (oldest first).
     * Used when loading the full history of a conversation.
     */
    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);

    /**
     * Returns a paginated view of messages in a conversation (newest first).
     * Used for paginated loading in the chat UI.
     */
    Page<Message> findByConversationIdOrderByCreatedAtDesc(Long conversationId, Pageable pageable);
}
