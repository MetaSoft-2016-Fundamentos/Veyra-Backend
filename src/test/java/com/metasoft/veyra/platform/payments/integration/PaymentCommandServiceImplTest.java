package com.metasoft.veyra.platform.payments.integration;

import com.metasoft.veyra.platform.payments.application.internal.commandservices.PaymentCommandServiceImpl;
import com.metasoft.veyra.platform.payments.domain.model.aggregates.Payment;
import com.metasoft.veyra.platform.payments.domain.model.aggregates.Subscription;
import com.metasoft.veyra.platform.payments.domain.model.commands.ProcessPaymentCommand;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.Amount;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.PaymentStatus;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.jpa.repositories.PaymentRepository;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.stripe.service.StripeService;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentCommandServiceImplTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private SubscriptionRepository subscriptionRepository;
    @Mock private StripeService stripeService;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PaymentCommandServiceImpl paymentCommandService;

    @Test
    @DisplayName("Debe procesar pago exitosamente cuando Stripe devuelve 'succeeded'")
    void handle_ProcessPayment_Succeeded() {

        ProcessPaymentCommand command = new ProcessPaymentCommand(1L, "pm_123");

        Subscription mockSub = mock(Subscription.class);
        when(mockSub.getAmount()).thenReturn(new Amount(new BigDecimal("100"), "USD"));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(mockSub));

        PaymentIntent mockIntent = new PaymentIntent();
        mockIntent.setStatus("succeeded");

        when(stripeService.createPaymentIntent(any(), any(), any())).thenReturn(mockIntent);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<Payment> result = paymentCommandService.handle(command);

        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo(PaymentStatus.SUCCEEDED);
        verify(eventPublisher, times(1)).publishEvent(any());
    }
}