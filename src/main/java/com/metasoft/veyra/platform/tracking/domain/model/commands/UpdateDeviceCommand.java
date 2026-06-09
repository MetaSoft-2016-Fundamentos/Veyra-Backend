package com.metasoft.veyra.platform.tracking.domain.model.commands;

import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.DeviceType;

public record UpdateDeviceCommand(Long id, DeviceType deviceType) {
}
