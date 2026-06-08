package com.metasoft.veyra.platform.activities.interfaces.rest.resources;

import java.util.List;

/**
 * REST resource representing the request body for creating a new activity.
 * The nursing home ID is provided via the request path, not this resource.
 * @param residentId the resident the activity is assigned to
 * @param healthcareStaffId the healthcare staff member responsible
 * @param type the activity type as a string (must match {@link com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityType})
 * @param title the descriptive title of the activity
 * @param isRecurring whether the activity repeats on specific days
 * @param recurringDays the days on which the activity recurs as strings (must match {@link com.metasoft.veyra.platform.activities.domain.model.valueobjects.RecurringDay})
 */
public record CreateActivityResource(
        Long residentId,
        Long healthcareStaffId,
        String type,
        String title,
        Boolean isRecurring,
        List<String> recurringDays
) {}
