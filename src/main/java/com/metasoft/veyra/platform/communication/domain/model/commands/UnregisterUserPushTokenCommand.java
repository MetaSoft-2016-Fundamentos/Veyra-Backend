package com.metasoft.veyra.platform.communication.domain.model.commands;

public record UnregisterUserPushTokenCommand(
        Long userId,
        String token
) {
    public UnregisterUserPushTokenCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("FCM token cannot be null or blank");
        }
    }
}
