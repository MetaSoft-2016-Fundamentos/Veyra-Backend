package com.metasoft.veyra.platform.payments.domain.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long paymentId) {
        super("Payment not found with id: " + paymentId);
    }

    public PaymentNotFoundException(String stripePaymentIntentId) {
        super("Payment not found with Stripe Payment Intent ID: " + stripePaymentIntentId);
    }
}