package com.metasoft.veyra.platform.communication.application.internal.commandservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.communication.domain.exceptions.UserNotificationNotFoundException;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserNotification;
import com.metasoft.veyra.platform.communication.domain.model.commands.CreateUserNotificationCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.MarkAllNotificationsAsReadCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.MarkNotificationAsReadCommand;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.NotificationStatus;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.UserNotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserNotificationCommandServiceImplTest {

    @Mock
    private UserNotificationRepository userNotificationRepository;

    @Mock
    private ExternalIamService externalIamService;

    @InjectMocks
    private UserNotificationCommandServiceImpl userNotificationCommandService;

    @Test
    void shouldCreateUnreadNotification() {
        CreateUserNotificationCommand command = new CreateUserNotificationCommand(1L, "Title", "Body");
        when(userNotificationRepository.save(any(UserNotification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserNotification notification = userNotificationCommandService.handle(command);

        assertEquals(NotificationStatus.UNREAD, notification.getStatus());
        verify(externalIamService).ensureUserExists(1L);
    }

    @Test
    void shouldMarkNotificationAsRead() {
        MarkNotificationAsReadCommand command = new MarkNotificationAsReadCommand(1L, 10L);
        UserNotification notification = new UserNotification(1L, "Title", "Body");
        when(userNotificationRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(notification));
        when(userNotificationRepository.save(notification)).thenReturn(notification);

        UserNotification updated = userNotificationCommandService.handle(command);

        assertEquals(NotificationStatus.READ, updated.getStatus());
        assertEquals(updated.getReadAt(), notification.getReadAt());
    }

    @Test
    void shouldMarkAllNotificationsAsRead() {
        MarkAllNotificationsAsReadCommand command = new MarkAllNotificationsAsReadCommand(1L);
        UserNotification first = new UserNotification(1L, "A", "Body");
        UserNotification second = new UserNotification(1L, "B", "Body");
        when(userNotificationRepository.findByUserIdAndStatus(1L, NotificationStatus.UNREAD))
                .thenReturn(List.of(first, second));

        int markedCount = userNotificationCommandService.handle(command);

        assertEquals(2, markedCount);
        assertEquals(NotificationStatus.READ, first.getStatus());
        assertEquals(NotificationStatus.READ, second.getStatus());
        verify(userNotificationRepository).saveAll(List.of(first, second));
    }

    @Test
    void shouldThrowWhenNotificationNotFoundOnMarkAsRead() {
        MarkNotificationAsReadCommand command = new MarkNotificationAsReadCommand(1L, 99L);
        when(userNotificationRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(UserNotificationNotFoundException.class, () -> userNotificationCommandService.handle(command));
    }
}
