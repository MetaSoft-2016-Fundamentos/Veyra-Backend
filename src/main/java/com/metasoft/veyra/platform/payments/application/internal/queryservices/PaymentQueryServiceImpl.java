package com.metasoft.veyra.platform.payments.application.internal.queryservices;

import com.metasoft.veyra.platform.payments.domain.model.aggregates.Payment;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetPaymentByIdQuery;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetPaymentByStripePaymentIntentIdQuery;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetPaymentBySubscriptionIdQuery;
import com.metasoft.veyra.platform.payments.domain.model.queries.GetPaymentsByUserIdQuery;
import com.metasoft.veyra.platform.payments.domain.services.PaymentQueryService;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.jpa.repositories.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final PaymentRepository paymentRepository;

    public PaymentQueryServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Optional<Payment> findById(GetPaymentByIdQuery query) {
        log.info("Finding payment by ID: {}", query.paymentId());

        Optional<Payment> payment = paymentRepository.findById(query.paymentId());

        if (payment.isPresent()) {
            log.info("Payment found: {}", payment.get().getId());
        } else {
            log.warn("Payment not found with ID: {}", query.paymentId());
        }

        return payment;
    }

    @Override
    public List<Payment> handle(GetPaymentsByUserIdQuery query) {
        log.info("Finding all payments for user: {}", query.userId());

        List<Payment> payments = paymentRepository.findByUserId(query.userId());

        log.info("Found {} payment(s) for user: {}", payments.size(), query.userId());

        return payments;
    }

    @Override
    public List<Payment> handle(GetPaymentBySubscriptionIdQuery query) {
        log.info("Finding all payments for subscription: {}", query.subscriptionId());

        List<Payment> payments = paymentRepository.findBySubscriptionId(query.subscriptionId());

        log.info("Found {} payment(s) for subscription: {}", payments.size(), query.subscriptionId());

        return payments;
    }

    @Override
    public Optional<Payment> handle(GetPaymentByStripePaymentIntentIdQuery query) {
        log.info("Finding payment by Stripe Payment Intent ID: {}", query.stripePaymentIntentId());

        Optional<Payment> payment = paymentRepository
                .findByStripePaymentIntentId(query.stripePaymentIntentId());

        if (payment.isPresent()) {
            log.info("Payment found with Stripe Payment Intent ID: {}", query.stripePaymentIntentId());
        } else {
            log.warn("Payment not found with Stripe Payment Intent ID: {}", query.stripePaymentIntentId());
        }

        return payment;
    }
}