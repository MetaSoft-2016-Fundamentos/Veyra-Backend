package com.metasoft.veyra.platform.communication.infrastructure.providers.fcm;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationCommand;

public final class FirebasePushMessageBuilder {

    private FirebasePushMessageBuilder() {
    }

    public static Message buildMessage(SendPushNotificationCommand command) {
        var builder = Message.builder()
                .setToken(command.deviceToken())
                .setNotification(Notification.builder()
                        .setTitle(command.title())
                        .setBody(command.body())
                        .build());

        if (command.notificationId() != null) {
            builder.putData("notificationId", String.valueOf(command.notificationId()));
        }

        return builder.build();
    }
}
