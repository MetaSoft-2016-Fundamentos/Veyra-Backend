package com.metasoft.veyra.platform.activities.interfaces.rest.transform;

import com.metasoft.veyra.platform.activities.domain.model.commands.CreateActivityCommand;
import com.metasoft.veyra.platform.activities.interfaces.rest.resources.CreateActivityResource;

public class CreateActivityCommandFromResourceAssembler {

    public static CreateActivityCommand toCommandFromResource(
            CreateActivityResource resource,
            Long nursingHomeId) {
        return new CreateActivityCommand(
                resource.name(),
                resource.activityDate(),
                resource.startTime(),
                resource.endTime(),
                resource.area(),
                nursingHomeId,
                resource.residentId(),
                resource.attendantId()
        );
    }
}