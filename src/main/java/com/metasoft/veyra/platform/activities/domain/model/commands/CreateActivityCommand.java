package com.metasoft.veyra.platform.activities.domain.model.commands;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateActivityCommand(
        String name,
        LocalDate activityDate,
        LocalTime startTime,
        LocalTime endTime,
        String area,
        Long nursingHomeId,
        Long residentId,
        Long staffMemberId
) {}