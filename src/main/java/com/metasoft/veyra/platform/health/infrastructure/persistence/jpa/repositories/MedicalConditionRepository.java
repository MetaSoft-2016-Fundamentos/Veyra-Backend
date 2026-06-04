package com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.health.domain.model.aggregates.MedicalCondition;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalConditionRepository extends JpaRepository<MedicalCondition, Long> {
  List<MedicalCondition> findAllByResidentId(ResidentId residentId);
  boolean existsByResidentIdAndDiagnosisName(ResidentId residentId, String diagnosisName);
}
