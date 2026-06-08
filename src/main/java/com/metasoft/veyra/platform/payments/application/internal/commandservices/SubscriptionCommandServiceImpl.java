package com.metasoft.veyra.platform.payments.application.internal.commandservices;

import com.metasoft.veyra.platform.payments.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.payments.domain.exceptions.DuplicateActiveSubscriptionException;
import com.metasoft.veyra.platform.payments.domain.exceptions.SubscriptionNotFoundException;
import com.metasoft.veyra.platform.payments.domain.model.aggregates.Subscription;
import com.metasoft.veyra.platform.payments.domain.model.commands.CancelSubscriptionCommand;
import com.metasoft.veyra.platform.payments.domain.model.commands.CreateSubscriptionCommand;
import com.metasoft.veyra.platform.payments.domain.model.commands.UpdateSubscriptionCommand;
import com.metasoft.veyra.platform.payments.domain.model.events.SubscriptionCanceledEvent;
import com.metasoft.veyra.platform.payments.domain.model.events.SubscriptionCreatedEvent;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.PlanType;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.SubscriptionPeriod;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.SubscriptionStatus;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.UserId;
import com.metasoft.veyra.platform.payments.domain.services.SubscriptionCommandService;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.stripe.service.StripeService;
import com.stripe.model.Customer;
import com.stripe.model.SubscriptionItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@Slf4j
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    private final SubscriptionRepository subscriptionRepository;
    private final StripeService stripeService;
    private final ApplicationEventPublisher eventPublisher;
    private final ExternalIamService externalIamService;

    public SubscriptionCommandServiceImpl(
            SubscriptionRepository subscriptionRepository,
            StripeService stripeService,
            ApplicationEventPublisher eventPublisher,
            ExternalIamService externalIamService) {
        this.subscriptionRepository = subscriptionRepository;
        this.stripeService = stripeService;
        this.eventPublisher = eventPublisher;
        this.externalIamService = externalIamService;
    }

    @Override
    public Optional<Subscription> handle(CreateSubscriptionCommand command) {
        log.info("Creating subscription for userId: {}", command.userId());

        if (!externalIamService.existsUserById(command.userId())) {
            log.error("User {} not found in IAM system, cannot create subscription", command.userId());
            throw new IllegalArgumentException("User not found: " + command.userId());
        }

        var userId = new UserId(command.userId());

        try {
            boolean hasActiveSubscription = subscriptionRepository
                    .existsByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);

            if (hasActiveSubscription) {
                throw new DuplicateActiveSubscriptionException(command.userId());
            }

            var planType = PlanType.valueOf(command.planType().toUpperCase());
            var period = SubscriptionPeriod.valueOf(command.period().toUpperCase());
            log.info("Plan: {}, Period: {}", planType, period);

            Customer customer = stripeService.createOrGetCustomer(command.userId(), null);
            log.info("Stripe customer ID: {}", customer.getId());
            com.stripe.model.Subscription stripeSubscription = stripeService.createSubscription(
                    customer.getId(),
                    planType,
                    period,
                    command.paymentMethodId()
            );
            log.info("Stripe subscription created: {}", stripeSubscription.getId());
            var subscription = new Subscription(
                    userId,
                    stripeSubscription.getId(),
                    customer.getId(),
                    planType,
                    period
            );
            updateSubscriptionFromStripe(subscription, stripeSubscription);
            var savedSubscription = subscriptionRepository.save(subscription);
            log.info("Subscription saved with ID: {}", savedSubscription.getId());
            var event = new SubscriptionCreatedEvent(
                    this,
                    savedSubscription.getId(),
                    savedSubscription.getUserId().userId(),
                    savedSubscription.getStripeSubscriptionId()
            );
            eventPublisher.publishEvent(event);
            log.info("SubscriptionCreatedEvent published");

            return Optional.of(savedSubscription);

        } catch (IllegalArgumentException e) {
            log.error("Invalid plan type or period: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error creating subscription: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create subscription", e);
        }
    }
    @Override
    public Optional<Subscription> handle(UpdateSubscriptionCommand command) {
        log.info("Updating subscription ID: {}", command.subscriptionId());
        try {
            var subscription = subscriptionRepository
                    .findById(command.subscriptionId())
                    .orElseThrow(() -> new SubscriptionNotFoundException(command.subscriptionId()));

            if (!subscription.isActive()) {
                throw new RuntimeException("Cannot update inactive subscription");
            }

            var newPlanType = PlanType.valueOf(command.planType().toUpperCase());
            var newPeriod = SubscriptionPeriod.valueOf(command.period().toUpperCase());
            log.info("Updating to Plan: {}, Period: {}", newPlanType, newPeriod);

            var stripeSubscription = stripeService.updateSubscription(
                    subscription.getStripeSubscriptionId(),
                    newPlanType,
                    newPeriod
            );
            log.info("Stripe subscription updated: {}", stripeSubscription.getId());

            updateSubscriptionFromStripe(subscription, stripeSubscription);

            var updatedSubscription = subscriptionRepository.save(subscription);
            log.info("Subscription updated successfully");

            return Optional.of(updatedSubscription);

        } catch (IllegalArgumentException e) {
            log.error("Invalid plan type or period: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error updating subscription: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update subscription", e);
        }
    }
    @Override
    public Optional<Subscription> handle(CancelSubscriptionCommand command) {
        log.info("Canceling subscription ID: {}", command.subscriptionId());
        try {
            var subscription = subscriptionRepository
                    .findById(command.subscriptionId())
                    .orElseThrow(() -> new SubscriptionNotFoundException(command.subscriptionId()));

            if (subscription.getStatus() == SubscriptionStatus.CANCELED) {
                log.warn("Subscription {} is already canceled", command.subscriptionId());
                return Optional.of(subscription);
            }

            com.stripe.model.Subscription stripeSubscription = stripeService.cancelSubscription(
                    subscription.getStripeSubscriptionId()
            );
            log.info("Stripe subscription canceled: {}", stripeSubscription.getId());

            subscription.cancel();
            var canceledSubscription = subscriptionRepository.save(subscription);
            log.info("Subscription canceled successfully");

            SubscriptionCanceledEvent event = new SubscriptionCanceledEvent(
                    this,
                    canceledSubscription.getId(),
                    canceledSubscription.getUserId().userId()
            );
            eventPublisher.publishEvent(event);
            log.info("SubscriptionCanceledEvent published");

            return Optional.of(canceledSubscription);

        } catch (Exception e) {
            log.error("Error canceling subscription: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to cancel subscription", e);
        }
    }

    private void updateSubscriptionFromStripe(
            Subscription subscription,
            com.stripe.model.Subscription stripeSubscription) {
        var status = mapStripeStatus(stripeSubscription.getStatus());
        subscription.updateStatus(status);

        if (status == SubscriptionStatus.ACTIVE) {
            var items = stripeSubscription.getItems();
            if (items != null && items.getData() != null && !items.getData().isEmpty()) {
                SubscriptionItem firstItem = items.getData().get(0);

                Long currentPeriodStart = firstItem.getCurrentPeriodStart();
                Long currentPeriodEnd = firstItem.getCurrentPeriodEnd();

                if (currentPeriodStart != null && currentPeriodEnd != null) {
                    var periodStart = LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(currentPeriodStart),
                            ZoneId.systemDefault()
                    );
                    var periodEnd = LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(currentPeriodEnd),
                            ZoneId.systemDefault()
                    );
                    subscription.activate(periodStart, periodEnd);
                    log.info("Subscription periods set from first item: start={}, end={}",
                            periodStart, periodEnd);
                } else {
                    log.warn("Current period dates are null in subscription item for subscription: {}",
                            stripeSubscription.getId());
                }
            } else {
                log.warn("No subscription items found for subscription: {}",
                        stripeSubscription.getId());
                Long billingCycleAnchor = stripeSubscription.getBillingCycleAnchor();
                if (billingCycleAnchor != null) {
                    var periodStart = LocalDateTime.ofInstant(
                            Instant.ofEpochSecond(billingCycleAnchor),
                            ZoneId.systemDefault()
                    );
                    var periodEnd = periodStart.plusMonths(1);
                    subscription.activate(periodStart, periodEnd);
                    log.info("Using billing cycle anchor as fallback for subscription: {}",
                            stripeSubscription.getId());
                }
            }
        }
    }

    private SubscriptionStatus mapStripeStatus(String stripeStatus) {
        return switch (stripeStatus.toLowerCase()) {
            case "active" -> SubscriptionStatus.ACTIVE;
            case "past_due" -> SubscriptionStatus.PAST_DUE;
            case "canceled" -> SubscriptionStatus.CANCELED;
            case "incomplete" -> SubscriptionStatus.INCOMPLETE;
            case "incomplete_expired" -> SubscriptionStatus.INCOMPLETE_EXPIRED;
            case "trialing" -> SubscriptionStatus.TRIALING;
            case "unpaid" -> SubscriptionStatus.UNPAID;
            default -> {
                log.warn("Unknown Stripe status: {}, defaulting to INCOMPLETE", stripeStatus);
                yield SubscriptionStatus.INCOMPLETE;
            }
        };
    }
}