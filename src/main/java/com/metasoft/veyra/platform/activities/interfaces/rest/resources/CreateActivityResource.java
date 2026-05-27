package com.metasoft.veyra.platform.activities.interfaces.rest.resources;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateActivityResource(
        String name,
        LocalDate activityDate,
        LocalTime startTime,
        LocalTime endTime,
        String area,
        Long residentId,
        Long attendantId
) {}
