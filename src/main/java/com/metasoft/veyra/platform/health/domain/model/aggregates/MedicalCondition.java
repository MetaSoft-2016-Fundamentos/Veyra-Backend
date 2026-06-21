package com.metasoft.veyra.platform.health.domain.model.aggregates;

import com.metasoft.veyra.platform.health.domain.model.valueobjects.DiagnosisStatus;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class MedicalCondition extends AuditableAbstractAggregateRoot<MedicalCondition> {

  @Embedded
  private ResidentId residentId;

  @Column(nullable = false)
  private String diagnosisName;

  @Column(nullable = false)
  private LocalDate diagnosisDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DiagnosisStatus status;

  @Column(length = 500)
  private String notes;

  public MedicalCondition() {}

  public MedicalCondition(ResidentId residentId, String diagnosisName, LocalDate diagnosisDate, DiagnosisStatus status, String notes) {
    this.residentId = residentId;
    this.diagnosisName = diagnosisName;
    this.diagnosisDate = diagnosisDate;
    this.status = status;
    this.notes = notes;
  }
}
