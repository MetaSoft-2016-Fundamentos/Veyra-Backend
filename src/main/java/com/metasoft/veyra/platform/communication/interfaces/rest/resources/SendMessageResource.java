package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SendMessageResource(
        @NotNull(message = "Sender user ID is required")
        Long senderUserId,

        @NotBlank(message = "Message content must not be blank")
        @Size(max = 2000, message = "Message content must not exceed 2000 characters")
        String content
) {
}
