package com.metasoft.veyra.platform.communication.application.internal.commandservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.communication.domain.exceptions.UserNotificationNotFoundException;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserNotification;
import com.metasoft.veyra.platform.communication.domain.model.commands.CreateUserNotificationCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.MarkAllNotificationsAsReadCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.MarkNotificationAsReadCommand;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.NotificationStatus;
import com.metasoft.veyra.platform.communication.domain.services.UserNotificationCommandService;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.UserNotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserNotificationCommandServiceImpl implements UserNotificationCommandService {

    private final UserNotificationRepository userNotificationRepository;
    private final ExternalIamService externalIamService;

    public UserNotificationCommandServiceImpl(
            UserNotificationRepository userNotificationRepository,
            ExternalIamService externalIamService
    ) {
        this.userNotificationRepository = userNotificationRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public UserNotification handle(CreateUserNotificationCommand command) {
        externalIamService.ensureUserExists(command.userId());
        return userNotificationRepository.save(
                new UserNotification(command.userId(), command.title(), command.body())
        );
    }

    @Override
    @Transactional
    public UserNotification handle(MarkNotificationAsReadCommand command) {
        externalIamService.ensureUserExists(command.userId());

        var notification = userNotificationRepository.findByIdAndUserId(command.notificationId(), command.userId())
                .orElseThrow(() -> new UserNotificationNotFoundException(command.userId(), command.notificationId()));

        if (notification.getStatus() != NotificationStatus.READ) {
            notification.markAsRead();
            return userNotificationRepository.save(notification);
        }

        return notification;
    }

    @Override
    @Transactional
    public int handle(MarkAllNotificationsAsReadCommand command) {
        externalIamService.ensureUserExists(command.userId());

        var unreadNotifications = userNotificationRepository.findByUserIdAndStatus(
                command.userId(),
                NotificationStatus.UNREAD
        );

        unreadNotifications.forEach(UserNotification::markAsRead);
        userNotificationRepository.saveAll(unreadNotifications);
        return unreadNotifications.size();
    }
}
