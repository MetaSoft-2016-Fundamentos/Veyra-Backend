package com.metasoft.veyra.platform.communication.domain.model.commands;

public record CreateUserNotificationCommand(
        Long userId,
        String title,
        String body
) {
    public CreateUserNotificationCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Notification title cannot be null or blank");
        }
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Notification body cannot be null or blank");
        }
    }
}
