package com.metasoft.veyra.platform.payments.infrastructure.persistence.stripe.webhooks;


import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StripeWebhookHandler {

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    /**
     * Verifica y procesa un evento de webhook de Stripe
     */
    public Event constructEvent(String payload, String sigHeader) {
        try {
            return Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (Exception e) {
            log.error("Error verifying webhook signature: {}", e.getMessage());
            throw new RuntimeException("Invalid webhook signature", e);
        }
    }

    /**
     * Procesa eventos relacionados con suscripciones
     */
    public void handleSubscriptionEvent(Event event) {
        String eventType = event.getType();
        log.info("Processing subscription event: {}", eventType);

        switch (eventType) {
            case "customer.subscription.created":
                handleSubscriptionCreated(event);
                break;
            case "customer.subscription.updated":
                handleSubscriptionUpdated(event);
                break;
            case "customer.subscription.deleted":
                handleSubscriptionDeleted(event);
                break;
            case "customer.subscription.trial_will_end":
                handleSubscriptionTrialEnding(event);
                break;
            default:
                log.info("Unhandled subscription event type: {}", eventType);
        }
    }

    /**
     * Procesa eventos relacionados con pagos
     */
    public void handlePaymentEvent(Event event) {
        String eventType = event.getType();
        log.info("Processing payment event: {}", eventType);

        switch (eventType) {
            case "payment_intent.succeeded":
                handlePaymentSucceeded(event);
                break;
            case "payment_intent.payment_failed":
                handlePaymentFailed(event);
                break;
            case "payment_intent.canceled":
                handlePaymentCanceled(event);
                break;
            case "payment_intent.processing":
                handlePaymentProcessing(event);
                break;
            default:
                log.info("Unhandled payment event type: {}", eventType);
        }
    }

    private void handleSubscriptionCreated(Event event) {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Unable to deserialize subscription"));

        log.info("Subscription created: {}", subscription.getId());
        // Aquí se publicará un evento de dominio para que la capa de aplicación lo maneje
    }

    private void handleSubscriptionUpdated(Event event) {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Unable to deserialize subscription"));

        log.info("Subscription updated: {}, status: {}",
                subscription.getId(), subscription.getStatus());
    }

    private void handleSubscriptionDeleted(Event event) {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Unable to deserialize subscription"));

        log.info("Subscription deleted: {}", subscription.getId());
    }

    private void handleSubscriptionTrialEnding(Event event) {
        Subscription subscription = (Subscription) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Unable to deserialize subscription"));

        log.info("Subscription trial ending: {}", subscription.getId());
        // Aquí podrías enviar un email de notificación
    }

    private void handlePaymentSucceeded(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Unable to deserialize payment intent"));

        log.info("Payment succeeded: {}, amount: {}",
                paymentIntent.getId(), paymentIntent.getAmount());
    }

    private void handlePaymentFailed(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Unable to deserialize payment intent"));

        log.error("Payment failed: {}, reason: {}",
                paymentIntent.getId(),
                paymentIntent.getLastPaymentError() != null
                        ? paymentIntent.getLastPaymentError().getMessage()
                        : "Unknown");
    }

    private void handlePaymentCanceled(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Unable to deserialize payment intent"));

        log.info("Payment canceled: {}", paymentIntent.getId());
    }

    private void handlePaymentProcessing(Event event) {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new RuntimeException("Unable to deserialize payment intent"));

        log.info("Payment processing: {}", paymentIntent.getId());
    }
}