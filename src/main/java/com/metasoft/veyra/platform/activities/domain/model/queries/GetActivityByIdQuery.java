package com.metasoft.veyra.platform.activities.domain.model.queries;

/**
 * Query to retrieve a single activity by its ID.
 * @param activityId the ID of the activity to retrieve
 */
public record GetActivityByIdQuery(Long activityId) {}
