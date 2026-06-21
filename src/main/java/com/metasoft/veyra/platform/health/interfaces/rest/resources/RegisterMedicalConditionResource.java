package com.metasoft.veyra.platform.health.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record RegisterMedicalConditionResource(
  @NotNull(message = "Diagnosis name is required")
  String diagnosisName,

  @NotNull(message = "Diagnosis date is required")
  LocalDate diagnosisDate,

  @NotNull(message = "Status is required (ACTIVE, RESOLVED, CHRONIC)")
  String status,

  String notes
) {}
