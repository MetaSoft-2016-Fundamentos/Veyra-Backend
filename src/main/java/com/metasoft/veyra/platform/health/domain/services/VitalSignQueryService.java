package com.metasoft.veyra.platform.health.domain.services;

import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSign;
import com.metasoft.veyra.platform.health.domain.model.queries.GetVitalSignByIdQuery;
import com.metasoft.veyra.platform.health.domain.model.queries.GetVitalSignsByResidentIdQuery;

import java.util.List;
import java.util.Optional;
public interface VitalSignQueryService {
    List<VitalSign>handle(GetVitalSignsByResidentIdQuery query);
    Optional<VitalSign> handle(GetVitalSignByIdQuery query);
}
