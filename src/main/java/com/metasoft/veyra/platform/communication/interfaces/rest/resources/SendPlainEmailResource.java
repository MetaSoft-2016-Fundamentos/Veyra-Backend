package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SendPlainEmailResource(
        @Schema(example = "[\"user@example.com\"]")
        @NotEmpty(message = "Recipients are required")
        List<@Email(message = "Recipient email must be valid") String> recipients,

        @Schema(example = "Welcome to Veyra")
        @NotBlank(message = "Subject is required")
        @Size(max = 255, message = "Subject must not exceed 255 characters")
        String subject,

        @Schema(example = "Hello from Veyra")
        @NotBlank(message = "Plain content is required")
        String plainContent
) {
}
