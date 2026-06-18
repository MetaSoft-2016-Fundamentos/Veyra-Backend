package com.metasoft.veyra.platform.communication.domain.model.commands;

public record SendPushNotificationToUserCommand(
        Long userId,
        String title,
        String body
) {
    public SendPushNotificationToUserCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Push notification title cannot be null or blank");
        }
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Push notification body cannot be null or blank");
        }
    }
}
