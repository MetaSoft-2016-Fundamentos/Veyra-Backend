package com.metasoft.veyra.platform.communication.domain.services;

import com.metasoft.veyra.platform.communication.domain.model.commands.CreateConversationCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.MarkConversationAsReadCommand;
import com.metasoft.veyra.platform.communication.domain.model.results.CreateConversationResult;

public interface ConversationCommandService {
    CreateConversationResult handle(CreateConversationCommand command);

    void handle(MarkConversationAsReadCommand command);
}
