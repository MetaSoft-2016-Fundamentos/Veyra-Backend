package com.metasoft.veyra.platform.health.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record RegisterAllergyResource(
        @NotNull(message = "Reaction is required")
        String reaction,

        @NotNull(message = "Allergen name is required")
        String allergenName,

        @NotNull(message = "Type of allergy is required")
        String typeOfAllergy,

        @NotNull(message = "Severity level is required")
        String severityLevel
) {}
