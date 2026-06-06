package com.metasoft.veyra.platform.payments.application.internal.commandservices;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.PaymentStatus;
import com.metasoft.veyra.platform.payments.domain.exceptions.SubscriptionNotFoundException;
import com.metasoft.veyra.platform.payments.domain.model.aggregates.Payment;
import com.metasoft.veyra.platform.payments.domain.model.commands.ProcessPaymentCommand;
import com.metasoft.veyra.platform.payments.domain.model.events.PaymentFailedEvent;
import com.metasoft.veyra.platform.payments.domain.model.events.PaymentSucceededEvent;
import com.metasoft.veyra.platform.payments.domain.services.PaymentCommandService;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.jpa.repositories.PaymentRepository;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.stripe.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class PaymentCommandServiceImpl implements PaymentCommandService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final StripeService stripeService;
    private final ApplicationEventPublisher eventPublisher;
    public PaymentCommandServiceImpl(
            PaymentRepository paymentRepository,
            SubscriptionRepository subscriptionRepository,
            StripeService stripeService,
            ApplicationEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.stripeService = stripeService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Optional<Payment> handle(ProcessPaymentCommand command) {
        log.info("Processing payment for subscription ID: {}", command.subscriptionId());

        try {
           var subscription = subscriptionRepository
                    .findById(command.subscriptionId())
                    .orElseThrow(() -> new SubscriptionNotFoundException(command.subscriptionId()));

            log.info("Subscription found: {}", subscription.getId());

            long amountInCents = subscription.getAmount().value()
                    .multiply(new java.math.BigDecimal(100))
                    .longValue();

            PaymentIntent paymentIntent = stripeService.createPaymentIntent(
                    amountInCents,
                    subscription.getAmount().currency().toLowerCase(),
                    subscription.getStripeCustomerId()
            );

            log.info("PaymentIntent created: {}", paymentIntent != null ? paymentIntent.getId() : "null");

            Payment payment = new Payment(
                    subscription,
                    subscription.getUserId(),
                    paymentIntent.getId(),
                    subscription.getAmount()
            );

            updatePaymentStatusFromStripe(payment, paymentIntent);

            Payment savedPayment = paymentRepository.save(payment);
            log.info("Payment saved with ID: {}", savedPayment.getId());
            String status = paymentIntent.getStatus() != null ? paymentIntent.getStatus().toLowerCase() : "";
            if ("succeeded".equals(status)) {
                PaymentSucceededEvent event = new PaymentSucceededEvent(
                        this,
                        savedPayment.getId(),
                        savedPayment.getSubscription().getId(),
                        savedPayment.getAmount().value()
                );
                eventPublisher.publishEvent(event);
                log.info("PaymentSucceededEvent published");
            } else if ("failed".equals(status)) {
                PaymentFailedEvent event = new PaymentFailedEvent(
                        this,
                        savedPayment.getId(),
                        savedPayment.getSubscription().getId(),
                        savedPayment.getFailureMessage()
                );
                eventPublisher.publishEvent(event);
                log.info("PaymentFailedEvent published");
            }

            return Optional.of(savedPayment);

        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process payment", e);
        }
    }


    private void updatePaymentStatusFromStripe(Payment payment, PaymentIntent paymentIntent) {
        String status = paymentIntent.getStatus() != null ? paymentIntent.getStatus().toLowerCase() : "";

        switch (status) {
            case "succeeded": {
                Charge charge = paymentIntent.getLatestChargeObject();

                if (charge == null && paymentIntent.getLatestCharge() != null) {
                    try {
                        charge = Charge.retrieve(paymentIntent.getLatestCharge());
                    } catch (StripeException e) {
                        log.warn("No se pudo recuperar Charge por id {}: {}", paymentIntent.getLatestCharge(), e.getMessage());
                    }
                }

                String receiptUrl = charge != null ? charge.getReceiptUrl() : null;
                payment.markAsSucceeded(receiptUrl);
                log.info("Payment marked as SUCCEEDED (receiptUrl={})", receiptUrl);
                break;
            }

            case "processing":
                payment.markAsProcessing();
                log.info("Payment marked as PROCESSING");
                break;

            case "requires_payment_method":
            case "requires_confirmation":
            case "requires_action":
                log.info("Payment remains PENDING (status: {})", status);
                break;

            case "canceled":
                payment.updateStatus(PaymentStatus.CANCELED);
                log.info("Payment marked as CANCELED");
                break;

            default:
                if (paymentIntent.getLastPaymentError() != null) {
                    String errorMessage = paymentIntent.getLastPaymentError().getMessage();
                    payment.markAsFailed(errorMessage);
                    log.error("Payment marked as FAILED: {}", errorMessage);
                } else {
                    log.warn("PaymentIntent con estado no manejado: {}", status);
                }
                break;
        }
    }
}
