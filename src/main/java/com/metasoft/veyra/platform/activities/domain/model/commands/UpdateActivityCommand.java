package com.metasoft.veyra.platform.activities.domain.model.commands;

import com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityType;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.RecurringDay;

import java.util.List;

/**
 * Command to update the mutable fields of an existing activity.
 * @param activityId the ID of the activity to update
 * @param type the new activity type
 * @param title the new descriptive title
 * @param isRecurring whether the activity should repeat
 * @param recurringDays the new list of recurring days
 */
public record UpdateActivityCommand(
        Long activityId,
        ActivityType type,
        String title,
        Boolean isRecurring,
        List<RecurringDay> recurringDays
) {}
