package com.metasoft.veyra.platform.tracking.interfaces.rest.resources;

public record DeviceResource(Long id,
                             String macAddress,
                             Long nursingHomeId,
                             String deviceType,
                             Long residentId,
                             String assignedBy,
                             String assignedAt,
                             String lastSync,
                             String status) {
}
