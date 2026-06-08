package com.metasoft.veyra.platform.payments.interfaces.rest.resources;

public record PaymentResource(
        Long id,
        Long subscriptionId,
        String stripePaymentIntentId,
        Double amount,
        String currency,
        String status,
        String receiptUrl
) {}