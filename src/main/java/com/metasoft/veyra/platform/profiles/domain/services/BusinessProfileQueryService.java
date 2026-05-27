package com.metasoft.veyra.platform.profiles.domain.services;

import com.metasoft.veyra.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetAllBusinessProfileQuery;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetBusinessProfileByIdQuery;
import com.metasoft.veyra.platform.profiles.domain.model.queries.GetBusinessProfileByRucQuery;

import java.util.List;
import java.util.Optional;

public interface BusinessProfileQueryService {
    Optional<BusinessProfile>handle(GetBusinessProfileByIdQuery query);
    List<BusinessProfile> handle (GetAllBusinessProfileQuery query);
    Optional<BusinessProfile> handle (GetBusinessProfileByRucQuery query);
}
