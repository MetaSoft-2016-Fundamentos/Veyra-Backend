package com.metasoft.veyra.platform.communication.infrastructure.providers.stub;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.fcm.FirebaseMessagingGateway;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(name = "integrations.fcm.enabled", havingValue = "false")
public class DevFirebaseMessagingGatewayStub implements FirebaseMessagingGateway {

    @Override
    public void send(SendPushNotificationCommand command) {
        log.info("FCM integration disabled. Push notification not sent (deviceToken={}, title={})",
                command.deviceToken(), command.title());
    }
}
