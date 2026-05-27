package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

import java.time.LocalDate;

public record MedicationResource(Long id, Long residentId, String name, String description, Integer amount, LocalDate expirationDate, String drugPresentation, String dosage) {
}
