package com.metasoft.veyra.platform.tracking.domain.model.commands;


public record RegisterDeviceCommand(Long nursingHomeId, String  deviceType, String macAddress) {
}
