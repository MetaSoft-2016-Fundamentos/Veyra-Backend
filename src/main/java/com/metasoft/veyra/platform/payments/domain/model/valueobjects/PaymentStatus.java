package com.metasoft.veyra.platform.payments.domain.model.valueobjects;


public enum PaymentStatus {
    PENDING,
    PROCESSING,
    SUCCEEDED,
    FAILED,
    CANCELED,
    REFUNDED
}