package com.metasoft.veyra.platform.payments.infrastructure.persistence.stripe.service;

import com.metasoft.veyra.platform.payments.domain.model.valueobjects.PlanType;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.SubscriptionPeriod;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;

public interface StripeService {
    Customer createOrGetCustomer(Long userId, String email);
    com.stripe.model.Subscription createSubscription(
            String customerId,
            PlanType planType,
            SubscriptionPeriod period,
            String paymentMethodId
    );
    com.stripe.model.Subscription updateSubscription(
            String subscriptionId,
            PlanType planType,
            SubscriptionPeriod period
    );
    com.stripe.model.Subscription cancelSubscription(String subscriptionId);
    com.stripe.model.Subscription retrieveSubscription(String subscriptionId);
    PaymentIntent createPaymentIntent(Long amountInCents, String currency, String customerId);
    PaymentIntent retrievePaymentIntent(String paymentIntentId);
    String getPriceId(PlanType planType, SubscriptionPeriod period);
}