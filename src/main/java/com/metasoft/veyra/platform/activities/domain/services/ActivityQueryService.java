package com.metasoft.veyra.platform.activities.domain.services;

import com.metasoft.veyra.platform.activities.domain.model.queries.GetActivitiesByDateAndNursingHomeQuery;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityView;

import java.util.List;

public interface ActivityQueryService {
    List<ActivityView> handle(GetActivitiesByDateAndNursingHomeQuery query);
}