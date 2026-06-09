package com.metasoft.veyra.platform.tracking.interfaces.rest.transform;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.GpsLocation;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.LocationResource;

public class LocationResourceFromEntityAssembler {
    public static LocationResource toResourceFromEntity(GpsLocation location) {
        return new LocationResource(
                location.getId(),
                location.getDeviceId(),
                location.getLatitude(),
                location.getLongitude(),
                location.getRecordedAt() != null ? location.getRecordedAt().toString() : null
        );
    }
}
