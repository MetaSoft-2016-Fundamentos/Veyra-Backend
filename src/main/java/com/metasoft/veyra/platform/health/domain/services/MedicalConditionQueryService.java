package com.metasoft.veyra.platform.health.domain.services;

import com.metasoft.veyra.platform.health.domain.model.aggregates.MedicalCondition;
import com.metasoft.veyra.platform.health.domain.model.queries.GetMedicalConditionsByResidentIdQuery;

import java.util.List;
import java.util.Optional;

public interface MedicalConditionQueryService {
  List<MedicalCondition> handle(GetMedicalConditionsByResidentIdQuery query);
  Optional<MedicalCondition> handle(Long id);
}
