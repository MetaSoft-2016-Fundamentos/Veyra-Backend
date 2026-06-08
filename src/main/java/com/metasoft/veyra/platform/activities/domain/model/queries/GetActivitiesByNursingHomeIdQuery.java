package com.metasoft.veyra.platform.activities.domain.model.queries;

/**
 * Query to retrieve all activities belonging to a specific nursing home.
 * @param nursingHomeId the ID of the nursing home
 */
public record GetActivitiesByNursingHomeIdQuery(Long nursingHomeId) {}
