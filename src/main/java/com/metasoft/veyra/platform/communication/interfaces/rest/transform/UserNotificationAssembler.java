package com.metasoft.veyra.platform.communication.interfaces.rest.transform;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserNotification;
import com.metasoft.veyra.platform.communication.domain.model.commands.MarkAllNotificationsAsReadCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.MarkNotificationAsReadCommand;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetUnreadNotificationCountQuery;
import com.metasoft.veyra.platform.communication.domain.model.queries.GetUserNotificationsQuery;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.NotificationStatus;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.UserNotificationResource;

public final class UserNotificationAssembler {

    private UserNotificationAssembler() {
    }

    public static GetUserNotificationsQuery toQuery(Long userId, NotificationStatus status) {
        return new GetUserNotificationsQuery(userId, status);
    }

    public static GetUnreadNotificationCountQuery toUnreadCountQuery(Long userId) {
        return new GetUnreadNotificationCountQuery(userId);
    }

    public static MarkNotificationAsReadCommand toMarkAsReadCommand(Long userId, Long notificationId) {
        return new MarkNotificationAsReadCommand(userId, notificationId);
    }

    public static MarkAllNotificationsAsReadCommand toMarkAllAsReadCommand(Long userId) {
        return new MarkAllNotificationsAsReadCommand(userId);
    }

    public static UserNotificationResource toResource(UserNotification notification) {
        return new UserNotificationResource(
                notification.getId(),
                notification.getUserId(),
                notification.getTitle(),
                notification.getBody(),
                notification.getStatus().name(),
                notification.getCreatedAt().toString(),
                notification.getReadAt() != null ? notification.getReadAt().toString() : null
        );
    }
}
