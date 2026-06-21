package com.metasoft.veyra.platform.health.domain.model.commands;

import java.time.LocalDate;

public record RegisterMedicalConditionCommand(
  Long residentId,
  String diagnosisName,
  LocalDate diagnosisDate,
  String status,
  String notes
) {
  public RegisterMedicalConditionCommand {
    if (residentId == null) {
      throw new IllegalArgumentException("Resident id cannot be null");
    }
    if (diagnosisName == null || diagnosisName.isBlank()) {
      throw new IllegalArgumentException("Diagnosis name cannot be null or blank");
    }
    if (diagnosisDate == null) {
      throw new IllegalArgumentException("Diagnosis date cannot be null");
    }
    if (status == null || status.isBlank()) {
      throw new IllegalArgumentException("Status cannot be null or blank");
    }
  }
}
