package com.metasoft.veyra.platform.communication.domain.services;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.Message;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendMessageCommand;

public interface MessageCommandService {
    Message handle(SendMessageCommand command);
}
