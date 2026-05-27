package com.metasoft.veyra.platform.health.interfaces.rest.resources;

public record  AllergyResource(Long id,Long residentId,String allergenName,String reaction,String severityLevel,String typeOfAllergy) {
}
