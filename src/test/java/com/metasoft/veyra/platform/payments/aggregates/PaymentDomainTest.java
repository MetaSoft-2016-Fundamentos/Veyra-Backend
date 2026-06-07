package com.metasoft.veyra.platform.payments.aggregates;

import com.metasoft.veyra.platform.payments.domain.model.aggregates.Payment;
import com.metasoft.veyra.platform.payments.domain.model.aggregates.Subscription;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.*;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class PaymentDomainTest {

    @Nested
    @DisplayName("Tests para el Agregado Payment")
    class PaymentAggregateTests {

        private Payment crearPagoBase() {
            Subscription mockSubscription = mock(Subscription.class);
            UserId userId = new UserId(1L);
            Amount amount = new Amount(new BigDecimal("99.99"), "USD");
            return new Payment(mockSubscription, userId, "pi_12345", amount);
        }

        @Test
        @DisplayName("Debe inicializarse en estado PENDING")
        void testInicializacion_StatusPending() {
            Payment payment = crearPagoBase();
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
        }

        @Test
        @DisplayName("Debe cambiar a SUCCEEDED y asignar URL")
        void testMarkAsSucceeded_Exito() {
            Payment payment = crearPagoBase();
            payment.markAsSucceeded("https://stripe.com/receipt/123");
            assertThat(payment.getStatus()).isEqualTo(PaymentStatus.SUCCEEDED);
        }
    }

    @Nested
    @DisplayName("Tests para el Agregado Subscription")
    class SubscriptionAggregateTests {

        private Subscription crearSuscripcionBase() {
            UserId userId = new UserId(1L);
            return new Subscription(userId, "sub_123", "cus_456", PlanType.FAMILY, SubscriptionPeriod.MONTHLY);
        }

        @Test
        @DisplayName("Debe inicializarse con estado INCOMPLETE")
        void testInicializacion_StatusIncomplete() {
            Subscription subscription = crearSuscripcionBase();
            assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.INCOMPLETE);
        }

        @Test
        @DisplayName("Debe activar la suscripción exitosamente")
        void testActivate_Exito() {
            Subscription subscription = crearSuscripcionBase();
            subscription.activate(LocalDateTime.now(), LocalDateTime.now().plusMonths(1));
            assertThat(subscription.getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
            assertThat(subscription.isActive()).isTrue();
        }
    }
}