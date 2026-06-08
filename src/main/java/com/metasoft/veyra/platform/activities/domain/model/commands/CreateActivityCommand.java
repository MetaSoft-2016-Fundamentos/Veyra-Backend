package com.metasoft.veyra.platform.activities.domain.model.commands;

import com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityType;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.RecurringDay;

import java.util.List;

/**
 * Command to create a new activity within a nursing home.
 * @param nursingHomeId the ID of the nursing home this activity belongs to
 * @param residentId the ID of the resident the activity is assigned to
 * @param healthcareStaffId the ID of the healthcare staff member responsible
 * @param type the type of activity
 * @param title the descriptive title of the activity
 * @param isRecurring whether the activity repeats on specific days
 * @param recurringDays the list of days on which the activity recurs
 */
public record CreateActivityCommand(
        Long nursingHomeId,
        Long residentId,
        Long healthcareStaffId,
        ActivityType type,
        String title,
        Boolean isRecurring,
        List<RecurringDay> recurringDays
) {}
