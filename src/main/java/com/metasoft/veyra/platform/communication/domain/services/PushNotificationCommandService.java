package com.metasoft.veyra.platform.communication.domain.services;

import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationToUserCommand;
import com.metasoft.veyra.platform.communication.domain.model.results.SendPushNotificationToUserResult;

public interface PushNotificationCommandService {
    void handle(SendPushNotificationCommand command);

    SendPushNotificationToUserResult handle(SendPushNotificationToUserCommand command);
}
