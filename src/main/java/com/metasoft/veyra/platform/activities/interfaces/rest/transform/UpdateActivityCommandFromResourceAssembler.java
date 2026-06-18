package com.metasoft.veyra.platform.activities.interfaces.rest.transform;

import com.metasoft.veyra.platform.activities.domain.model.commands.UpdateActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityType;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.RecurringDay;
import com.metasoft.veyra.platform.activities.interfaces.rest.resources.UpdateActivityResource;

import java.util.List;

/**
 * Assembler that converts an {@link UpdateActivityResource} into an {@link UpdateActivityCommand}.
 */
public class UpdateActivityCommandFromResourceAssembler {

    /**
     * Converts an {@link UpdateActivityResource} and the path-provided activity ID into an {@link UpdateActivityCommand}.
     * @param activityId the activity ID extracted from the request path
     * @param resource the REST resource containing the updated activity data
     * @return the corresponding domain command
     */
    public static UpdateActivityCommand toCommandFromResource(Long activityId, UpdateActivityResource resource) {
        List<RecurringDay> days = resource.recurringDays() != null
                ? resource.recurringDays().stream().map(RecurringDay::valueOf).toList()
                : List.of();
        return new UpdateActivityCommand(
                activityId,
                ActivityType.valueOf(resource.type()),
                resource.title(),
                resource.isRecurring() != null && resource.isRecurring(),
                days
        );
    }
}
