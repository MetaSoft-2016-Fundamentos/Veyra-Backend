package com.metasoft.veyra.platform.payments.domain.exceptions;

public class DuplicateActiveSubscriptionException extends RuntimeException {
    public DuplicateActiveSubscriptionException(Long userId) {
        super("User " + userId + " already has an active subscription");
    }
}