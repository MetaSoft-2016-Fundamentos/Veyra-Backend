package com.metasoft.veyra.platform.tracking.application.acl;

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
    public Long fetchResidentIdByDeviceId(Long deviceId) {
        var getDeviceByIdQuery= new GetDeviceByIdQuery(deviceId);
         var device= deviceQueryService.handle(getDeviceByIdQuery);
         return device.isEmpty()?Long.valueOf(0L):device.get().getId();

    }
}
