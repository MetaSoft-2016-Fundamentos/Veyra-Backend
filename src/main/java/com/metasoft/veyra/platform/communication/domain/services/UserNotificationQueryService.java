package com.metasoft.veyra.platform.communication.domain.services;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserNotification;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetUnreadNotificationCountQuery;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetUserNotificationsQuery;

import java.util.List;

public interface UserNotificationQueryService {
    List<UserNotification> handle(GetUserNotificationsQuery query);

    long handle(GetUnreadNotificationCountQuery query);
}
