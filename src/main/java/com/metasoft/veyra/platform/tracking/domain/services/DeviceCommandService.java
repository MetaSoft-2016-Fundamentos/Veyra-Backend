package com.metasoft.veyra.platform.tracking.domain.services;

import com.metasoft.veyra.platform.tracking.domain.model.commands.AssignDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.ChangeDeviceStatusCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.RegisterDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.UnassignDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.UpdateDeviceCommand;

public interface DeviceCommandService {
    Long handle(AssignDeviceCommand command);
    void handle(UnassignDeviceCommand command);
    Long handle(RegisterDeviceCommand command);
    Long handle(UpdateDeviceCommand command);
    Long handle(ChangeDeviceStatusCommand command);
}
