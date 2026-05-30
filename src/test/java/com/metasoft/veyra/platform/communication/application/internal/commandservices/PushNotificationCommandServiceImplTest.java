package com.metasoft.veyra.platform.communication.application.internal.commandservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.fcm.FirebaseMessagingGateway;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PushNotificationCommandServiceImplTest {

    @Mock
    private FirebaseMessagingGateway firebaseMessagingGateway;

    @InjectMocks
    private PushNotificationCommandServiceImpl pushNotificationCommandService;

    @Test
    void shouldDelegatePushNotificationToGateway() {
        SendPushNotificationCommand command = new SendPushNotificationCommand(
                "device-token",
                "Alert",
                "New message"
        );

        pushNotificationCommandService.handle(command);

        verify(firebaseMessagingGateway).send(command);
    }
}
