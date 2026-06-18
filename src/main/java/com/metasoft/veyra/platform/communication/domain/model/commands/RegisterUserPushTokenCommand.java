package com.metasoft.veyra.platform.communication.domain.model.commands;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.PushPlatform;

public record RegisterUserPushTokenCommand(
        Long userId,
        String token,
        PushPlatform platform
) {
    public RegisterUserPushTokenCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("FCM token cannot be null or blank");
        }
        if (platform == null) {
            throw new IllegalArgumentException("Platform cannot be null");
        }
    }
}
