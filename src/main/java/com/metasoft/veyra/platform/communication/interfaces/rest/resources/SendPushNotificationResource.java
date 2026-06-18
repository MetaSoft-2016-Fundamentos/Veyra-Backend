package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendPushNotificationResource(
        @Schema(example = "fcm-device-token")
        @NotBlank(message = "Device token is required")
        String deviceToken,

        @Schema(example = "New alert")
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @Schema(example = "You have a new message")
        @NotBlank(message = "Body is required")
        String body
) {
}
