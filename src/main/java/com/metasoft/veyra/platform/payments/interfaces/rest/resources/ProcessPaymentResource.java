package com.metasoft.veyra.platform.payments.interfaces.rest.resources;

public record ProcessPaymentResource(
        Long subscriptionId,
        String paymentMethodId
) {}
