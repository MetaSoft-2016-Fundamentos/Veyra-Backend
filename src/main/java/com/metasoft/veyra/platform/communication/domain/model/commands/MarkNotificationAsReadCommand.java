package com.metasoft.veyra.platform.communication.domain.model.commands;

public record MarkNotificationAsReadCommand(
        Long userId,
        Long notificationId
) {
    public MarkNotificationAsReadCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (notificationId == null) {
            throw new IllegalArgumentException("Notification ID cannot be null");
        }
    }
}
