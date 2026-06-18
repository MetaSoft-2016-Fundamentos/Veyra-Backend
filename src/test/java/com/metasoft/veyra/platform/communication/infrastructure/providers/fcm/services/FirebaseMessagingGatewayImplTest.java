package com.metasoft.veyra.platform.communication.infrastructure.providers.fcm.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.metasoft.veyra.platform.communication.domain.exceptions.CommunicationProviderNotConfiguredException;
import com.metasoft.veyra.platform.communication.domain.exceptions.FirebaseMessagingIntegrationException;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationCommand;
import com.metasoft.veyra.platform.communication.infrastructure.providers.fcm.configuration.FirebaseMessagingSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FirebaseMessagingGatewayImplTest {

    @Mock
    private FirebaseMessaging firebaseMessaging;

    private final FirebaseMessagingSettings firebaseMessagingSettings = new FirebaseMessagingSettings(
            "veyra-project",
            "/path/to/service-account.json"
    );

    @Test
    void shouldSendPushNotificationWhenFirebaseAcceptsRequest() throws FirebaseMessagingException {
        FirebaseMessagingGatewayImpl gateway = new FirebaseMessagingGatewayImpl(firebaseMessagingSettings, firebaseMessaging);
        when(firebaseMessaging.send(any(Message.class))).thenReturn("message-id-123");

        SendPushNotificationCommand command = new SendPushNotificationCommand(
                "device-token",
                "Alert",
                "New message"
        );

        gateway.send(command);

        verify(firebaseMessaging).send(any(Message.class));
    }

    @Test
    void shouldThrowWhenFirebaseProjectIdIsMissing() {
        FirebaseMessagingGatewayImpl gateway = new FirebaseMessagingGatewayImpl(
                new FirebaseMessagingSettings("", "/path/to/service-account.json"),
                firebaseMessaging
        );

        SendPushNotificationCommand command = new SendPushNotificationCommand(
                "device-token",
                "Alert",
                "New message"
        );

        assertThrows(CommunicationProviderNotConfiguredException.class, () -> gateway.send(command));
    }

    @Test
    void shouldThrowWhenFirebaseRejectsRequest() throws FirebaseMessagingException {
        FirebaseMessagingGatewayImpl gateway = new FirebaseMessagingGatewayImpl(firebaseMessagingSettings, firebaseMessaging);
        FirebaseMessagingException exception = mock(FirebaseMessagingException.class);
        when(exception.getMessagingErrorCode()).thenReturn(MessagingErrorCode.INVALID_ARGUMENT);
        when(firebaseMessaging.send(any(Message.class))).thenThrow(exception);

        SendPushNotificationCommand command = new SendPushNotificationCommand(
                "device-token",
                "Alert",
                "New message"
        );

        assertThrows(FirebaseMessagingIntegrationException.class, () -> gateway.send(command));
    }
}
