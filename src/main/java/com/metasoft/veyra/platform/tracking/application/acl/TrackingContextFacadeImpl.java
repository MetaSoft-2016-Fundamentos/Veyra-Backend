package com.metasoft.veyra.platform.tracking.application.acl;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Device;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDeviceByIdQuery;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceQueryService;
import com.metasoft.veyra.platform.tracking.interfaces.acl.TrackingContextFacade;
import org.springframework.stereotype.Service;



@Service
public class TrackingContextFacadeImpl implements TrackingContextFacade {
    private final DeviceQueryService deviceQueryService;

    public TrackingContextFacadeImpl(DeviceQueryService deviceQueryService) {
        this.deviceQueryService = deviceQueryService;
    }
    @Override
    public Long fetchResidentIdByDeviceId(String deviceId) {
        return deviceQueryService.handle(new GetDeviceByIdQuery(deviceId))
                .filter(Device::isAssigned).map(Device::getResidentId).orElse(null);
    }
}
