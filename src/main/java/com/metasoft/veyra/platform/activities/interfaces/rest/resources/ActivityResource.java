package com.metasoft.veyra.platform.activities.interfaces.rest.resources;

public record ActivityResource(
        Long activityId,
        String hour,
        String attendantName,
        String activityName,
        String areaToDevelop,
        String status
) {}

