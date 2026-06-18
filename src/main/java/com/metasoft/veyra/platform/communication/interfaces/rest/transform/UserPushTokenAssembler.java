package com.metasoft.veyra.platform.communication.interfaces.rest.transform;

import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserPushToken;
import com.metasoft.veyra.platform.communication.domain.model.commands.RegisterUserPushTokenCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationToUserCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.UnregisterUserPushTokenCommand;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.RegisterUserPushTokenResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.SendPushNotificationToUserResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.UnregisterUserPushTokenResource;
import com.metasoft.veyra.platform.communication.interfaces.rest.resources.UserPushTokenResource;

public final class UserPushTokenAssembler {

    private UserPushTokenAssembler() {
    }

    public static RegisterUserPushTokenCommand toRegisterCommand(Long userId, RegisterUserPushTokenResource resource) {
        return new RegisterUserPushTokenCommand(userId, resource.token(), resource.platform());
    }

    public static UnregisterUserPushTokenCommand toUnregisterCommand(Long userId, UnregisterUserPushTokenResource resource) {
        return new UnregisterUserPushTokenCommand(userId, resource.token());
    }

    public static SendPushNotificationToUserCommand toSendToUserCommand(Long userId, SendPushNotificationToUserResource resource) {
        return new SendPushNotificationToUserCommand(userId, resource.title(), resource.body());
    }

    public static UserPushTokenResource toResource(UserPushToken token) {
        return new UserPushTokenResource(
                token.getId(),
                token.getUserId(),
                token.getToken(),
                token.getPlatform(),
                token.getLastSeenAt().toString()
        );
    }
}
