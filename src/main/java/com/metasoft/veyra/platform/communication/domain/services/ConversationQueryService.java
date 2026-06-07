package com.metasoft.veyra.platform.communication.domain.services;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.Conversation;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetConversationByIdQuery;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetConversationsByUserQuery;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetUnreadConversationCountQuery;

import java.util.List;
import java.util.Optional;

public interface ConversationQueryService {
    List<Conversation> handle(GetConversationsByUserQuery query);

    Optional<Conversation> handle(GetConversationByIdQuery query);

    long handle(GetUnreadConversationCountQuery query);
}
