package com.metasoft.veyra.platform.nursing.domain.services;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Relative;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetRelativeByIdQuery;

import java.util.Optional;
public interface RelativeQueryService {
    Optional<Relative>handle(GetRelativeByIdQuery query);
}
