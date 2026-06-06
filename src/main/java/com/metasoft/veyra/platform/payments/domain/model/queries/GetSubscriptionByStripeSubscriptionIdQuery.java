package com.metasoft.veyra.platform.payments.domain.model.queries;

public record GetSubscriptionByStripeSubscriptionIdQuery(String stripeSubscriptionId) {
    public GetSubscriptionByStripeSubscriptionIdQuery {
        if (stripeSubscriptionId == null || stripeSubscriptionId.isBlank()) {
            throw new IllegalArgumentException("Stripe Subscription ID cannot be null or blank");
        }
    }
}