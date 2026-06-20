package com.metasoft.veyra.platform.tracking.interfaces.rest.transform;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Location;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.LocationResource;

public class LocationResourceFromEntityAssembler {
    public static LocationResource toResourceFromEntity(Location location) {
        return new LocationResource(
                location.getId(),
                location.getDeviceId(),
                location.getCoordinates().latitude(),
                location.getCoordinates().longitude(),
                location.getRecordedAt() != null ? location.getRecordedAt().toString() : null
        );
    }
}
