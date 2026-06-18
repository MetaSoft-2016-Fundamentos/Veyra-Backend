package com.metasoft.veyra.platform.communication.domain.services;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserPushToken;
import com.metasoft.veyra.platform.communication.domain.model.commands.RegisterUserPushTokenCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.UnregisterUserPushTokenCommand;

public interface UserPushTokenCommandService {
    UserPushToken handle(RegisterUserPushTokenCommand command);

    void handle(UnregisterUserPushTokenCommand command);
}
