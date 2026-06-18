package com.metasoft.veyra.platform.communication.domain.services;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserNotification;
import com.metasoft.veyra.platform.communication.domain.model.commands.CreateUserNotificationCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.MarkAllNotificationsAsReadCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.MarkNotificationAsReadCommand;

public interface UserNotificationCommandService {
    UserNotification handle(CreateUserNotificationCommand command);

    UserNotification handle(MarkNotificationAsReadCommand command);

    int handle(MarkAllNotificationsAsReadCommand command);
}
