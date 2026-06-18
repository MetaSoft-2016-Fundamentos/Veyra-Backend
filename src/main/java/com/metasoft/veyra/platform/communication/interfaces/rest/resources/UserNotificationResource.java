package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

public record UserNotificationResource(
        Long id,
        Long userId,
        String title,
        String body,
        String status,
        String createdAt,
        String readAt
) {
}
