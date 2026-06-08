package com.metasoft.veyra.platform.activities.interfaces.rest.transform;

import com.metasoft.veyra.platform.activities.domain.model.commands.CreateActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityType;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.RecurringDay;
import com.metasoft.veyra.platform.activities.interfaces.rest.resources.CreateActivityResource;

import java.util.List;

/**
 * Assembler that converts a {@link CreateActivityResource} into a {@link CreateActivityCommand}.
 */
public class CreateActivityCommandFromResourceAssembler {

    /**
     * Converts a {@link CreateActivityResource} and the path-provided nursing home ID into a {@link CreateActivityCommand}.
     * @param nursingHomeId the nursing home ID extracted from the request path
     * @param resource the REST resource containing the activity creation data
     * @return the corresponding domain command
     */
    public static CreateActivityCommand toCommandFromResource(Long nursingHomeId, CreateActivityResource resource) {
        List<RecurringDay> days = resource.recurringDays() != null
                ? resource.recurringDays().stream().map(RecurringDay::valueOf).toList()
                : List.of();
        return new CreateActivityCommand(
                nursingHomeId,
                resource.residentId(),
                resource.healthcareStaffId(),
                ActivityType.valueOf(resource.type()),
                resource.title(),
                resource.isRecurring() != null && resource.isRecurring(),
                days
        );
    }
}
