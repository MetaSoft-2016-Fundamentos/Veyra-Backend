package com.metasoft.veyra.platform.health.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.tracking.interfaces.acl.TrackingContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExternalTrackingService {
private final TrackingContextFacade trackingContextFacade;

    public ExternalTrackingService(TrackingContextFacade trackingContextFacade) {
        this.trackingContextFacade = trackingContextFacade;
    }
    public Optional<ResidentId> fetchDeviceIdByResidentId(Long deviceId) {
        var residentId = trackingContextFacade.fetchResidentIdByDeviceId(deviceId);
        if (residentId == null || residentId == 0L) {
            return Optional.empty();
        }
        return Optional.of(new ResidentId(residentId));
    }
}
