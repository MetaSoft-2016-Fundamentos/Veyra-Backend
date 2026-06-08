package com.metasoft.veyra.platform.activities.domain.services;

import com.metasoft.veyra.platform.activities.domain.model.aggregates.Activity;
import com.metasoft.veyra.platform.activities.domain.model.queries.GetActivitiesByNursingHomeIdQuery;
import com.metasoft.veyra.platform.activities.domain.model.queries.GetActivitiesByResidentIdQuery;
import com.metasoft.veyra.platform.activities.domain.model.queries.GetActivityByIdQuery;
import com.metasoft.veyra.platform.activities.domain.model.queries.GetAllActivitiesQuery;

import java.util.List;
import java.util.Optional;

/**
 * ActivityQueryService
 * <p>Service interface that handles read operations on the Activity aggregate.</p>
 */
public interface ActivityQueryService {
    /**
     * Returns all activities in the system.
     * @param query the query object
     * @return a list of all activities
     */
    List<Activity> handle(GetAllActivitiesQuery query);

    /**
     * Returns all activities belonging to a specific nursing home.
     * @param query the query containing the nursing home ID
     * @return a list of activities for the given nursing home
     * @see GetActivitiesByNursingHomeIdQuery
     */
    List<Activity> handle(GetActivitiesByNursingHomeIdQuery query);

    /**
     * Returns all activities assigned to a specific resident.
     * @param query the query containing the resident ID
     * @return a list of activities for the given resident
     * @see GetActivitiesByResidentIdQuery
     */
    List<Activity> handle(GetActivitiesByResidentIdQuery query);

    /**
     * Returns a single activity by its ID.
     * @param query the query containing the activity ID
     * @return an {@link java.util.Optional} containing the activity, or empty if not found
     * @see GetActivityByIdQuery
     */
    Optional<Activity> handle(GetActivityByIdQuery query);
}
