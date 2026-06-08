package com.metasoft.veyra.platform.activities.interfaces.rest.resources;

import java.util.List;

/**
 * REST resource representing an Activity response.
 * @param id the activity ID
 * @param nursingHomeId the nursing home this activity belongs to
 * @param residentId the resident assigned to this activity
 * @param healthcareStaffId the healthcare staff member responsible
 * @param type the activity type
 * @param title the descriptive title
 * @param status the current lifecycle status
 * @param isRecurring whether the activity repeats
 * @param recurringDays the days on which the activity recurs
 */
public record ActivityResource(
        Long id,
        Long nursingHomeId,
        Long residentId,
        Long healthcareStaffId,
        String type,
        String title,
        String status,
        Boolean isRecurring,
        List<String> recurringDays
) {}
