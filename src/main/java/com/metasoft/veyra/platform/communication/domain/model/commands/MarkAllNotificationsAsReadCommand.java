package com.metasoft.veyra.platform.communication.domain.model.commands;

public record MarkAllNotificationsAsReadCommand(Long userId) {
    public MarkAllNotificationsAsReadCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }
}
