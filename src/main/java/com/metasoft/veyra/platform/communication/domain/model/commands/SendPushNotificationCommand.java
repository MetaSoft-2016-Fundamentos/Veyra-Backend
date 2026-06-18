package com.metasoft.veyra.platform.communication.domain.model.commands;

public record SendPushNotificationCommand(
        String deviceToken,
        String title,
        String body,
        Long notificationId
) {
    public SendPushNotificationCommand(String deviceToken, String title, String body) {
        this(deviceToken, title, body, null);
    }

    public SendPushNotificationCommand {
        if (deviceToken == null || deviceToken.isBlank()) {
            throw new IllegalArgumentException("Device token cannot be null or blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Push notification title cannot be null or blank");
        }
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Push notification body cannot be null or blank");
        }
    }
}
