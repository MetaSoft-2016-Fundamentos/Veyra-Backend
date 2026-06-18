package com.metasoft.veyra.platform.activities.domain.model.queries;

/**
 * Query to retrieve all activities assigned to a specific resident.
 * @param residentId the ID of the resident
 */
public record GetActivitiesByResidentIdQuery(Long residentId) {}
