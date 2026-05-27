package com.metasoft.veyra.platform.activities.domain.model.queries;

import java.time.LocalDate;

public record GetActivitiesByDateAndNursingHomeQuery(
        LocalDate date,
        Long nursingHomeId
) {}