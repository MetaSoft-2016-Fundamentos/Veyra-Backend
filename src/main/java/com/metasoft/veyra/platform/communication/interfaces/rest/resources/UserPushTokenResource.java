package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.PushPlatform;

public record UserPushTokenResource(
        Long id,
        Long userId,
        String token,
        PushPlatform platform,
        String lastSeenAt
) {
}
