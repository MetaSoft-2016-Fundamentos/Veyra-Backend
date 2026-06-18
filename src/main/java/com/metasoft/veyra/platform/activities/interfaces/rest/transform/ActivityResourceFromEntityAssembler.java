package com.metasoft.veyra.platform.activities.interfaces.rest.transform;

import com.metasoft.veyra.platform.activities.domain.model.aggregates.Activity;
import com.metasoft.veyra.platform.activities.interfaces.rest.resources.ActivityResource;

/**
 * Assembler that converts an {@link Activity} entity into an {@link ActivityResource} REST resource.
 */
public class ActivityResourceFromEntityAssembler {

    /**
     * Converts an {@link Activity} entity to an {@link ActivityResource}.
     * @param activity the activity entity to convert
     * @return the corresponding REST resource
     */
    public static ActivityResource toResourceFromEntity(Activity activity) {
        var recurringDays = activity.getRecurringDays().stream()
                .map(Enum::name)
                .toList();
        return new ActivityResource(
                activity.getId(),
                activity.getNursingHomeId(),
                activity.getResidentId(),
                activity.getHealthcareStaffId(),
                activity.getType().name(),
                activity.getTitle(),
                activity.getStatus().name(),
                activity.getIsRecurring(),
                recurringDays
        );
    }
}
