package com.metasoft.veyra.platform.payments.integration;

import com.metasoft.veyra.platform.payments.application.internal.commandservices.SubscriptionCommandServiceImpl;
import com.metasoft.veyra.platform.payments.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.payments.domain.model.aggregates.Subscription;
import com.metasoft.veyra.platform.payments.domain.model.commands.CreateSubscriptionCommand;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.SubscriptionStatus;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.jpa.repositories.SubscriptionRepository;
import com.metasoft.veyra.platform.payments.infrastructure.persistence.stripe.service.StripeService;
import com.stripe.model.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionCommandServiceImplTest {

    @Mock private SubscriptionRepository subscriptionRepository;
    @Mock private StripeService stripeService;
    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private ExternalIamService externalIamService;

    @InjectMocks
    private SubscriptionCommandServiceImpl subscriptionCommandService;

    @Test
    @DisplayName("Debe crear suscripción exitosamente cuando el usuario existe en IAM")
    void handle_CreateSubscription_Exito() {

        CreateSubscriptionCommand command = new CreateSubscriptionCommand(1L, "FAMILY", "MONTHLY", "pm_123");

        when(externalIamService.existsUserById(1L)).thenReturn(true);
        when(subscriptionRepository.existsByUserIdAndStatus(any(), eq(SubscriptionStatus.ACTIVE))).thenReturn(false);

        Customer mockCustomer = new Customer();
        mockCustomer.setId("cus_123");
        when(stripeService.createOrGetCustomer(1L, null)).thenReturn(mockCustomer);

        com.stripe.model.Subscription mockStripeSub = new com.stripe.model.Subscription();
        mockStripeSub.setId("sub_test_123");
        mockStripeSub.setStatus("active");
        when(stripeService.createSubscription(any(), any(), any(), any())).thenReturn(mockStripeSub);

        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Subscription> result = subscriptionCommandService.handle(command);

        assertThat(result).isPresent();
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el usuario no existe en IAM")
    void handle_CreateSubscription_FallaSiUsuarioNoExiste() {
        CreateSubscriptionCommand command = new CreateSubscriptionCommand(999L, "FAMILY", "MONTHLY", "pm_123");

        when(externalIamService.existsUserById(999L)).thenReturn(false);

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> subscriptionCommandService.handle(command));
    }
}