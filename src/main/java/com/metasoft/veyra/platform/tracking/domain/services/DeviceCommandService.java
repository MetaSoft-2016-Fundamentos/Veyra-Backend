package com.metasoft.veyra.platform.tracking.domain.services;

import com.metasoft.veyra.platform.tracking.domain.model.commands.AssignDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.SeedDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.UnassignDeviceCommand;

public interface DeviceCommandService {
    void handle(SeedDeviceCommand command);
    Long handle(AssignDeviceCommand command);
    void handle(UnassignDeviceCommand command);
}
