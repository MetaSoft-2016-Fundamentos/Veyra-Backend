package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.PushPlatform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterUserPushTokenResource(
        @Schema(example = "fcm-registration-token-from-firebase-sdk")
        @NotBlank(message = "FCM token is required")
        String token,

        @Schema(example = "ANDROID")
        @NotNull(message = "Platform is required")
        PushPlatform platform
) {
}
