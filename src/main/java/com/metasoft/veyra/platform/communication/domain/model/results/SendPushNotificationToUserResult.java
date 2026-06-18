package com.metasoft.veyra.platform.communication.domain.model.results;

public record SendPushNotificationToUserResult(
        Long notificationId,
        int deliveredCount
) {
}
