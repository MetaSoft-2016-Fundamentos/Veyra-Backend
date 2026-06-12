package com.metasoft.veyra.platform.tracking.interfaces.rest.transform;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Device;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.DeviceResource;

public class DeviceResourceFromEntityAssembler {
    public static DeviceResource toResourceFromEntity(Device entity) {
        var residentId= entity.getResidentId()!= null ? entity.getResidentId().residentId():null;
        return new DeviceResource(
               entity.getId(),
               entity.getNursingHomeId().nursingHomeId(),
               entity.getDeviceType().name(),
               entity.getStatus().name(),
               residentId,
               entity.getAssignedAt(),
               entity.getMacAddress().macAddress()
        );
    }
}