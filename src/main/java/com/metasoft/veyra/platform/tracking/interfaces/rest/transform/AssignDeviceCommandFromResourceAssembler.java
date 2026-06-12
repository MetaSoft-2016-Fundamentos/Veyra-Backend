package com.metasoft.veyra.platform.tracking.interfaces.rest.transform;

import com.metasoft.veyra.platform.tracking.domain.model.commands.AssignDeviceCommand;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.AssignDeviceResource;

public class AssignDeviceCommandFromResourceAssembler {
    public static AssignDeviceCommand toCommandFromResource(Long deviceId, AssignDeviceResource resource) {
        return new AssignDeviceCommand(
                deviceId,
                resource.residentId()
        );
    }
}
