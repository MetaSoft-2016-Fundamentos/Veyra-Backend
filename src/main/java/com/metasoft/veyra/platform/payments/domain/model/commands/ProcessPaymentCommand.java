package com.metasoft.veyra.platform.payments.domain.model.commands;

public record ProcessPaymentCommand(
        Long subscriptionId,
        String paymentMethodId
) {
    public ProcessPaymentCommand {
        if (subscriptionId == null) {
            throw new IllegalArgumentException("Subscription ID cannot be null");
        }
        if (paymentMethodId == null || paymentMethodId.isBlank()) {
            throw new IllegalArgumentException("Payment method ID cannot be null");
        }
    }
}