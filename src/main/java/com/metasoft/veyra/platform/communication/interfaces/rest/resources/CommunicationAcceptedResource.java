package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommunicationAcceptedResource(
        @Schema(example = "Email accepted for delivery")
        String message
) {
}
