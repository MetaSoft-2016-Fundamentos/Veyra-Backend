package com.metasoft.veyra.platform.health.domain.services;

import com.metasoft.veyra.platform.health.domain.model.aggregates.Allergy;
import com.metasoft.veyra.platform.health.domain.model.queries.ExistsAllergyByResidentIdQuery;
import com.metasoft.veyra.platform.health.domain.model.queries.GetAllergiesByResidentIdQuery;
import com.metasoft.veyra.platform.health.domain.model.queries.GetAllergyByIdQuery;

import java.util.List;
import java.util.Optional;

public interface AllergyQueryService {
    List<Allergy>handle(GetAllergiesByResidentIdQuery query);
    boolean handle(ExistsAllergyByResidentIdQuery query);
    Optional<Allergy> handle(GetAllergyByIdQuery query);
}
