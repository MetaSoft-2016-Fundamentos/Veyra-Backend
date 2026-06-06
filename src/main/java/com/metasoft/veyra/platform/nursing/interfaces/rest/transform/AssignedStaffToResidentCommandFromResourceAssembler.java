package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.commands.AssignedStaffMemberToResidentCommand;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.AssignedStaffToResidentResource;

public class AssignedStaffToResidentCommandFromResourceAssembler {
    public static AssignedStaffMemberToResidentCommand toCommandFromResource(Long nursingHomeId, Long residentId, AssignedStaffToResidentResource resource) {
        return new AssignedStaffMemberToResidentCommand(nursingHomeId, residentId, resource.staffId());
    }
}
