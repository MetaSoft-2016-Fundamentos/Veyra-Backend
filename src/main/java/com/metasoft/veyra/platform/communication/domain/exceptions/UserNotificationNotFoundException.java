package com.metasoft.veyra.platform.communication.domain.exceptions;

public class UserNotificationNotFoundException extends RuntimeException {
    public UserNotificationNotFoundException(Long userId, Long notificationId) {
        super("Notification " + notificationId + " not found for user " + userId);
    }
}
