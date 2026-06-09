package com.metasoft.veyra.platform.tracking.interfaces.rest.transform;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Device;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.DeviceResource;

public class DeviceResourceFromEntityAssembler {
    public static DeviceResource toResourceFromEntity(Device device) {
        String lastSync = device.getAssignedAt() != null ? device.getAssignedAt().toString() : null;
        return new DeviceResource(
                device.getId(),
                device.getDeviceId(),
                device.getNursingHomeId(),
                device.getDeviceType() != null ? device.getDeviceType().name() : null,
                device.getResidentId(),
                device.getAssignedBy(),
                lastSync,
                lastSync,
                device.getStatus().name()
        );
    }
}