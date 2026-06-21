package com.metasoft.veyra.platform.health.domain.services;

import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSignThreshold;
import com.metasoft.veyra.platform.health.domain.model.queries.GetVitalSignThresholdByResidentIdQuery;

import java.util.Optional;

public interface VitalSignThresholdQueryService {
    Optional<VitalSignThreshold> handle(GetVitalSignThresholdByResidentIdQuery query);
}
