package com.metasoft.veyra.platform.communication.domain.services;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.Message;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetConversationMessagesQuery;

import java.util.List;

public interface MessageQueryService {
    List<Message> handle(GetConversationMessagesQuery query);
}
