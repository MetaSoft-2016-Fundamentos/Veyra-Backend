package com.metasoft.veyra.platform.tracking.interfaces.rest.transform;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Device;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.DeviceResource;

public class DeviceResourceFromEntityAssembler {
    public static DeviceResource toResourceFromEntity(Device device) {
        return new DeviceResource(
                device.getId(),
                device.getDeviceId(),
                device.getResidentId(),
                device.getAssignedBy(),
                device.getAssignedAt() != null ? device.getAssignedAt().toString() : null,
                device.getStatus().name()
        );
    }
}