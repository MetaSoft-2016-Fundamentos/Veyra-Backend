package com.metasoft.veyra.platform.communication.application.internal.commandservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.communication.application.internal.outboundservices.fcm.FirebaseMessagingGateway;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserNotification;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserPushToken;
import com.metasoft.veyra.platform.communication.domain.model.commands.CreateUserNotificationCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationToUserCommand;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.PushPlatform;
import com.metasoft.veyra.platform.communication.domain.services.UserNotificationCommandService;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.UserPushTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PushNotificationCommandServiceImplTest {

    @Mock
    private FirebaseMessagingGateway firebaseMessagingGateway;

    @Mock
    private UserPushTokenRepository userPushTokenRepository;

    @Mock
    private UserNotificationCommandService userNotificationCommandService;

    @Mock
    private ExternalIamService externalIamService;

    @InjectMocks
    private PushNotificationCommandServiceImpl pushNotificationCommandService;

    @Test
    void shouldDelegatePushNotificationToGateway() {
        SendPushNotificationCommand command = new SendPushNotificationCommand(
                "device-token",
                "Title",
                "Body"
        );

        pushNotificationCommandService.handle(command);

        verify(firebaseMessagingGateway).send(command);
    }

    @Test
    void shouldPersistNotificationAndSendPushWithNotificationId() {
        SendPushNotificationToUserCommand command = new SendPushNotificationToUserCommand(1L, "Title", "Body");
        UserNotification notification = org.mockito.Mockito.mock(UserNotification.class);
        UserPushToken phone = new UserPushToken(1L, "phone-token", PushPlatform.ANDROID);

        when(notification.getId()).thenReturn(42L);
        when(userNotificationCommandService.handle(any(CreateUserNotificationCommand.class))).thenReturn(notification);
        when(userPushTokenRepository.findByUserId(1L)).thenReturn(List.of(phone));

        var result = pushNotificationCommandService.handle(command);

        assertEquals(42L, result.notificationId());
        assertEquals(1, result.deliveredCount());

        ArgumentCaptor<SendPushNotificationCommand> captor = ArgumentCaptor.forClass(SendPushNotificationCommand.class);
        verify(firebaseMessagingGateway).send(captor.capture());
        assertEquals(42L, captor.getValue().notificationId());
        assertEquals("phone-token", captor.getValue().deviceToken());
    }

    @Test
    void shouldPersistNotificationEvenWhenUserHasNoTokens() {
        SendPushNotificationToUserCommand command = new SendPushNotificationToUserCommand(1L, "Title", "Body");
        UserNotification notification = org.mockito.Mockito.mock(UserNotification.class);

        when(notification.getId()).thenReturn(7L);
        when(userNotificationCommandService.handle(any(CreateUserNotificationCommand.class))).thenReturn(notification);
        when(userPushTokenRepository.findByUserId(1L)).thenReturn(List.of());

        var result = pushNotificationCommandService.handle(command);

        assertEquals(7L, result.notificationId());
        assertEquals(0, result.deliveredCount());
    }
}
