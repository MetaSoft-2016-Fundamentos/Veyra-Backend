package com.metasoft.veyra.platform.health.application.internal.queryservices;

import com.metasoft.veyra.platform.health.domain.model.aggregates.MedicalCondition;
import com.metasoft.veyra.platform.health.domain.model.queries.GetMedicalConditionsByResidentIdQuery;
import com.metasoft.veyra.platform.health.domain.services.MedicalConditionQueryService;
import com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories.MedicalConditionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class MedicalConditionQueryServiceImpl implements MedicalConditionQueryService {

  private final MedicalConditionRepository medicalConditionRepository;

  public MedicalConditionQueryServiceImpl(MedicalConditionRepository medicalConditionRepository) {
    this.medicalConditionRepository = medicalConditionRepository;
  }

  @Override
  public List<MedicalCondition> handle(GetMedicalConditionsByResidentIdQuery query) {
    return medicalConditionRepository.findAllByResidentId(query.residentId());
  }

  @Override
  public Optional<MedicalCondition> handle(Long id) {
    return medicalConditionRepository.findById(id);
  }
}
