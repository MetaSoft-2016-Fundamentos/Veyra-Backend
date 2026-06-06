package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UnregisterUserPushTokenResource(
        @Schema(example = "fcm-registration-token-from-firebase-sdk")
        @NotBlank(message = "FCM token is required")
        String token
) {
}
