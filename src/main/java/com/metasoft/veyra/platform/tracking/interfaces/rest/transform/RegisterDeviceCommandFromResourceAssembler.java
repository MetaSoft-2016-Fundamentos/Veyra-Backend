package com.metasoft.veyra.platform.tracking.interfaces.rest.transform;

import com.metasoft.veyra.platform.tracking.domain.model.commands.RegisterDeviceCommand;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.RegisterDeviceResource;

public class RegisterDeviceCommandFromResourceAssembler {
    public static RegisterDeviceCommand toCommandFromResource(RegisterDeviceResource resource, Long nursingHomeId) {
        return new RegisterDeviceCommand(nursingHomeId, resource.deviceType(), resource.macAddress());
    }
}
