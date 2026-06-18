package com.metasoft.veyra.platform.activities.interfaces.rest.resources;

import java.util.List;

/**
 * REST resource representing the request body for updating an existing activity.
 * Fields id, residentId, healthcareStaffId and status are accepted but ignored —
 * they are controlled by the URL path or dedicated endpoints.
 * @param id ignored — activity ID comes from the path
 * @param residentId ignored — cannot be changed after creation
 * @param healthcareStaffId ignored — cannot be changed after creation
 * @param type the new activity type as a string
 * @param title the new descriptive title
 * @param status ignored — use PATCH /{activityId}/complete to advance status
 * @param isRecurring whether the activity should repeat
 * @param recurringDays the new list of recurring days as strings
 */
public record UpdateActivityResource(
        Long id,
        Long residentId,
        Long healthcareStaffId,
        String type,
        String title,
        String status,
        Boolean isRecurring,
        List<String> recurringDays
) {}
