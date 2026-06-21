package com.metasoft.veyra.platform.health.interfaces.rest.resources;

import java.time.LocalDate;

public record MedicalConditionResource(
  Long id,
  Long residentId,
  String diagnosisName,
  LocalDate diagnosisDate,
  String status,
  String notes
) {}
