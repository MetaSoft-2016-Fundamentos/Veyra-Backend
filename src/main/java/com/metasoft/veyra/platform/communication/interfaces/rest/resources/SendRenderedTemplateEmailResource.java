package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailTemplate;

public record SendRenderedTemplateEmailResource(
        @Schema(example = "[\"user@example.com\"]")
        @NotEmpty(message = "Recipients are required")
        List<@Email(message = "Recipient email must be valid") String> recipients,

        @Schema(example = "Bienvenido a Veyra")
        @NotBlank(message = "Subject is required")
        String subject,

        @Schema(example = "WELCOME", allowableValues = {"WELCOME", "SET_PASSWORD", "RESET_PASSWORD"})
        @NotNull(message = "Template is required")
        EmailTemplate template,

        @Schema(example = "{\"firstName\": \"Ana\", \"loginUrl\": \"https://veyra.pe/sign-in\", \"year\": \"2026\", \"recipientEmail\": \"user@example.com\"}")
        Map<String, String> variables
) {
}
