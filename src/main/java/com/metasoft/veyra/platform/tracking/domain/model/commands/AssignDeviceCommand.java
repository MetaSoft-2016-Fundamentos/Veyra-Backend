package com.metasoft.veyra.platform.tracking.domain.model.commands;

public record AssignDeviceCommand(String deviceId,
                                  Long residentId,
                                  String assignedBy) {
}
