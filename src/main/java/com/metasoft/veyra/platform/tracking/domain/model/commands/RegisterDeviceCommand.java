package com.metasoft.veyra.platform.tracking.domain.model.commands;

import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.DeviceType;

public record RegisterDeviceCommand(Long nursingHomeId, DeviceType deviceType, String macAddress) {
}
