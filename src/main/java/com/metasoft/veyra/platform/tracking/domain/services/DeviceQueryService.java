package com.metasoft.veyra.platform.tracking.domain.services;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Device;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetAllDevicesQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDeviceByIdQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDevicesByNursingHomeIdQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDevicesByResidentIdQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetUnassignedDevicesQuery;

import java.util.List;
import java.util.Optional;

public interface DeviceQueryService {
    List<Device> handle(GetAllDevicesQuery query);
    Optional<Device> handle(GetDeviceByIdQuery query);
    List<Device> handle(GetDevicesByResidentIdQuery query);
    List<Device> handle(GetUnassignedDevicesQuery query);
    List<Device> handle(GetDevicesByNursingHomeIdQuery query);
}
