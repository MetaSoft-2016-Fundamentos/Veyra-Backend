package com.metasoft.veyra.platform.payments.domain.model.commands;
public record UpdateSubscriptionCommand(
        Long subscriptionId,
        String planType,
        String period
) {
    public UpdateSubscriptionCommand {
        if (subscriptionId == null) {
            throw new IllegalArgumentException("Subscription ID cannot be null");
        }
    }
}