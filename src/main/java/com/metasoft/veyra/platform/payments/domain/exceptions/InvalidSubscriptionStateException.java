package com.metasoft.veyra.platform.payments.domain.exceptions;

public class InvalidSubscriptionStateException extends RuntimeException {
    public InvalidSubscriptionStateException(String message) {
        super(message);
    }
}