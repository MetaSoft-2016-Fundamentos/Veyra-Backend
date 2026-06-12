package com.metasoft.veyra.platform.tracking.domain.model.commands;

public record AssignDeviceCommand(Long deviceId,
                                  Long residentId
                                  ) {
}
