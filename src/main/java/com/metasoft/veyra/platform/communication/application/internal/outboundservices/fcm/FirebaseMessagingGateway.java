package com.metasoft.veyra.platform.communication.application.internal.outboundservices.fcm;

import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationCommand;

public interface FirebaseMessagingGateway {
    void send(SendPushNotificationCommand command);
}
