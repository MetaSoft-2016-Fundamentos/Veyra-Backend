package com.metasoft.veyra.platform.payments.application.internal.queryservices;

import com.metasoft.veyra.platform.payments.domain.model.aggregates.Subscription;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetSubscriptionActiveByUserId;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetSubscriptionByIdQuery;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetSubscriptionByStripeSubscriptionIdQuery;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetSubscriptionByUserId;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.SubscriptionStatus;
import com.metasoft.veyra.platform.payments.domain.services.SubscriptionQueryService;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionQueryServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Optional<Subscription> handle(GetSubscriptionByIdQuery query) {
        log.info("Finding subscription by ID: {}", query.subscriptionId());

        Optional<Subscription> subscription = subscriptionRepository.findById(query.subscriptionId());

        if (subscription.isPresent()) {
            log.info("Subscription found: {}", subscription.get().getId());
        } else {
            log.warn("Subscription not found with ID: {}", query.subscriptionId());
        }

        return subscription;
    }

    @Override
    public Optional<Subscription> handle(GetSubscriptionByStripeSubscriptionIdQuery query) {
        log.info("Finding subscription by Stripe ID: {}", query.stripeSubscriptionId());

        Optional<Subscription> subscription = subscriptionRepository
                .findByStripeSubscriptionId(query.stripeSubscriptionId());

        if (subscription.isPresent()) {
            log.info("Subscription found with Stripe ID: {}", query.stripeSubscriptionId());
        } else {
            log.warn("Subscription not found with Stripe ID: {}", query.stripeSubscriptionId());
        }

        return subscription;
    }

    @Override
    public List<Subscription> handle(GetSubscriptionByUserId query) {
        log.info("Finding all subscriptions for user: {}", query.userId());

        List<Subscription> subscriptions = subscriptionRepository.findByUserId(query.userId());

        log.info("Found {} subscription(s) for user: {}", subscriptions.size(), query.userId());

        return subscriptions;
    }

    @Override
    public Optional<Subscription> handle(GetSubscriptionActiveByUserId query) {
        log.info("Finding active subscription for user: {}", query.userId());

        Optional<Subscription> subscription = subscriptionRepository
                .findByUserIdAndStatus(query.userId(), SubscriptionStatus.ACTIVE);

        if (subscription.isPresent()) {
            log.info("Active subscription found for user: {}", query.userId());
        } else {
            log.info("No active subscription found for user: {}", query.userId());
        }

        return subscription;
    }
}