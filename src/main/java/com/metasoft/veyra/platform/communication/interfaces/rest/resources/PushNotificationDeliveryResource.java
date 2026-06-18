package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

public record PushNotificationDeliveryResource(
        String message,
        Long notificationId,
        int deliveredCount
) {
}
