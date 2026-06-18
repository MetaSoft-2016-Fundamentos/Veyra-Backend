package com.metasoft.veyra.platform.payments.infrastructure.persistence.stripe.stub;

import com.metasoft.veyra.platform.payments.domain.exceptions.StripeServiceException;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.PlanType;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.SubscriptionPeriod;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.stripe.service.StripeService;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(name = "integrations.stripe.enabled", havingValue = "false")
public class DevStripeServiceStub implements StripeService {

    private static final String MESSAGE =
            "Stripe integration is disabled. Set integrations.stripe.enabled=true and configure stripe.api.key.";

    @Override
    public Customer createOrGetCustomer(Long userId, String email) {
        return disabled("createOrGetCustomer");
    }

    @Override
    public com.stripe.model.Subscription createSubscription(
            String customerId,
            PlanType planType,
            SubscriptionPeriod period,
            String paymentMethodId) {
        return disabled("createSubscription");
    }

    @Override
    public com.stripe.model.Subscription updateSubscription(
            String subscriptionId,
            PlanType planType,
            SubscriptionPeriod period) {
        return disabled("updateSubscription");
    }

    @Override
    public com.stripe.model.Subscription cancelSubscription(String subscriptionId) {
        return disabled("cancelSubscription");
    }

    @Override
    public com.stripe.model.Subscription retrieveSubscription(String subscriptionId) {
        return disabled("retrieveSubscription");
    }

    @Override
    public PaymentIntent createPaymentIntent(Long amountInCents, String currency, String customerId) {
        return disabled("createPaymentIntent");
    }

    @Override
    public PaymentIntent retrievePaymentIntent(String paymentIntentId) {
        return disabled("retrievePaymentIntent");
    }

    @Override
    public String getPriceId(PlanType planType, SubscriptionPeriod period) {
        return disabled("getPriceId");
    }

    private <T> T disabled(String operation) {
        log.warn("Stripe {} skipped: {}", operation, MESSAGE);
        throw new StripeServiceException(MESSAGE);
    }
}
