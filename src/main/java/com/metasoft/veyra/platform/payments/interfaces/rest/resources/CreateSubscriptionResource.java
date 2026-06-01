package com.metasoft.veyra.platform.payments.interfaces.rest.resources;


public record CreateSubscriptionResource(
        String planType,
        String period,
        String paymentMethodId
) {}
