package com.metasoft.veyra.platform.communication.interfaces.rest.transform;

import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationCommand;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.SendPushNotificationResource;

public class SendPushNotificationCommandFromResourceAssembler {

    private SendPushNotificationCommandFromResourceAssembler() {
    }

    public static SendPushNotificationCommand toCommandFromResource(SendPushNotificationResource resource) {
        return new SendPushNotificationCommand(
                resource.deviceToken(),
                resource.title(),
                resource.body()
        );
    }
}
