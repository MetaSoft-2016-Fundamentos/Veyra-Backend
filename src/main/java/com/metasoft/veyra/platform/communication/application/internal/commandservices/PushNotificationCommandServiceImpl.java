package com.metasoft.veyra.platform.communication.application.internal.commandservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.communication.application.internal.outboundservices.fcm.FirebaseMessagingGateway;
import com.metasoft.veyra.platform.communication.domain.model.commands.CreateUserNotificationCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationToUserCommand;
import com.metasoft.veyra.platform.communication.domain.model.results.SendPushNotificationToUserResult;
import com.metasoft.veyra.platform.communication.domain.services.PushNotificationCommandService;
import com.metasoft.veyra.platform.communication.domain.services.UserNotificationCommandService;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.UserPushTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationCommandServiceImpl implements PushNotificationCommandService {

    private final FirebaseMessagingGateway firebaseMessagingGateway;
    private final UserPushTokenRepository userPushTokenRepository;
    private final UserNotificationCommandService userNotificationCommandService;
    private final ExternalIamService externalIamService;

    public PushNotificationCommandServiceImpl(
            FirebaseMessagingGateway firebaseMessagingGateway,
            UserPushTokenRepository userPushTokenRepository,
            UserNotificationCommandService userNotificationCommandService,
            ExternalIamService externalIamService
    ) {
        this.firebaseMessagingGateway = firebaseMessagingGateway;
        this.userPushTokenRepository = userPushTokenRepository;
        this.userNotificationCommandService = userNotificationCommandService;
        this.externalIamService = externalIamService;
    }

    @Override
    public void handle(SendPushNotificationCommand command) {
        firebaseMessagingGateway.send(command);
    }

    @Override
    public SendPushNotificationToUserResult handle(SendPushNotificationToUserCommand command) {
        externalIamService.ensureUserExists(command.userId());

        var notification = userNotificationCommandService.handle(
                new CreateUserNotificationCommand(command.userId(), command.title(), command.body())
        );

        var tokens = userPushTokenRepository.findByUserId(command.userId());
        int deliveredCount = 0;
        for (var userPushToken : tokens) {
            firebaseMessagingGateway.send(new SendPushNotificationCommand(
                    userPushToken.getToken(),
                    command.title(),
                    command.body(),
                    notification.getId()
            ));
            deliveredCount++;
        }

        return new SendPushNotificationToUserResult(notification.getId(), deliveredCount);
    }
}
