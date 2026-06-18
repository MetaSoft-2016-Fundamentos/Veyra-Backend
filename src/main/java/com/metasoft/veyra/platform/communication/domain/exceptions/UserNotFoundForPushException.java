package com.metasoft.veyra.platform.communication.domain.exceptions;

public class UserNotFoundForPushException extends RuntimeException {
    public UserNotFoundForPushException(Long userId) {
        super("User not found: " + userId);
    }
}
