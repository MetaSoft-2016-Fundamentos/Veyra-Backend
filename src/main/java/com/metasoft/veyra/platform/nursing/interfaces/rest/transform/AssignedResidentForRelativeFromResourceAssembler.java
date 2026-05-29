package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.commands.AssignResidentForRelativeCommand;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.AssignedRelativeForResidentResource;

public class AssignedResidentForRelativeFromResourceAssembler {
    public static AssignResidentForRelativeCommand toCommandFromResource(AssignedRelativeForResidentResource resource,Long residentId){
        return new AssignResidentForRelativeCommand(
                resource.residentId(),
                residentId

        );
    }
}
