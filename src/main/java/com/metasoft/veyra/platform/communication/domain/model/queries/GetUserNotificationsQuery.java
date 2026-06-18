package com.metasoft.veyra.platform.communication.domain.model.queries;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.NotificationStatus;

public record GetUserNotificationsQuery(
        Long userId,
        NotificationStatus status
) {
    public GetUserNotificationsQuery {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }

    public GetUserNotificationsQuery(Long userId) {
        this(userId, null);
    }
}
