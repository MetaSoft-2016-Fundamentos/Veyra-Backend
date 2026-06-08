package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreateMedicationResource(

        @NotBlank(message = "Medication name is required")
        String name,

        @NotBlank(message = "Medication description is required")
        String description,

        @NotNull(message = "Medication amount is required")
        Integer amount,

        @NotNull(message = "Expiration date is required")
        LocalDate expirationDate,

        @NotBlank(message = "Drug presentation is required")
        String drugPresentation,
        @NotBlank(message = "Dosage is required")
        String dosage
) {
}
