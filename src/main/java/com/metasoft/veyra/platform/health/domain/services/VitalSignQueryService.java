package com.metasoft.veyra.platform.health.domain.services;

import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSign;
import com.metasoft.veyra.platform.health.domain.model.queries.GetVitalSignByIdQuery;
import com.metasoft.veyra.platform.health.domain.model.queries.GetVitalSignsByResidentIdQuery;
import org.springframework.data.domain.Page;
import java.util.Optional;

public interface VitalSignQueryService {
  Page<VitalSign> handle(GetVitalSignsByResidentIdQuery query);
  Optional<VitalSign> handle(GetVitalSignByIdQuery query);
}
