package com.metasoft.veyra.platform.tracking.interfaces.rest.transform;

import com.metasoft.veyra.platform.tracking.domain.model.commands.UpdateDeviceCommand;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.RegisterDeviceResource;

public class UpdateDeviceCommandFromResourceAssembler {
    public static UpdateDeviceCommand toCommandFromResource(Long id, RegisterDeviceResource resource) {
        return new UpdateDeviceCommand(id, resource.deviceType(),resource.macAddress());
    }
}
