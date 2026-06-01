package com.metasoft.veyra.platform.payments.domain.model.queries;

public record GetPaymentByIdQuery(Long paymentId) {
    public GetPaymentByIdQuery {
        if (paymentId == null) {
            throw new IllegalArgumentException("Payment ID cannot be null");
        }
    }
}