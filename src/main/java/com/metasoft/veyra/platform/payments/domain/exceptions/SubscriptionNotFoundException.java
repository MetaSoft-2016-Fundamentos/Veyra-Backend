package com.metasoft.veyra.platform.payments.domain.exceptions;

public class SubscriptionNotFoundException extends RuntimeException {
    public SubscriptionNotFoundException(Long subscriptionId) {
        super("Subscription not found with id: " + subscriptionId);
    }

    public SubscriptionNotFoundException(String stripeSubscriptionId) {
        super("Subscription not found with Stripe ID: " + stripeSubscriptionId);
    }
}