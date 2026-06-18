package com.metasoft.veyra.platform.communication.domain.exceptions;

public class UserPushTokenNotFoundException extends RuntimeException {
    public UserPushTokenNotFoundException(Long userId) {
        super("No push tokens registered for user: " + userId);
    }

    public UserPushTokenNotFoundException(Long userId, String token) {
        super("Push token not found for user " + userId);
    }
}
