package com.metasoft.veyra.platform.payments.interfaces.rest.resources;

import java.time.LocalDateTime;

public record SubscriptionResource(
        Long id,
        Long userId,
        String stripeSubscriptionId,
        String planType,
        String period,
        Double amount,
        String currency,
        String status,
        LocalDateTime currentPeriodStart,
        LocalDateTime currentPeriodEnd,
        LocalDateTime createdAt
) {}