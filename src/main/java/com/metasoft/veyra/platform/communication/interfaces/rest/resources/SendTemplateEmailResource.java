package com.metasoft.veyra.platform.communication.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Map;

public record SendTemplateEmailResource(
        @Schema(example = "[\"user@example.com\"]")
        @NotEmpty(message = "Recipients are required")
        List<@Email(message = "Recipient email must be valid") String> recipients,

        @Schema(example = "d-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
        @NotBlank(message = "Template id is required")
        String templateId,

        @Schema(example = "{\"firstName\": \"Ana\", \"activationUrl\": \"https://veyra.com/activate\"}")
        Map<String, Object> dynamicTemplateData
) {
}
