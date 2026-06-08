package com.metasoft.veyra.platform.health.interfaces.rest.transform;

import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSign;
import com.metasoft.veyra.platform.health.interfaces.rest.resources.VitalSignResource;

public class VitalSignResourceFromEntityAssembler {
    public static VitalSignResource toResourceFromEntity(VitalSign entity){
        return new VitalSignResource(entity.getId(),entity.getResidentId().residentId(),entity.getMeasurementId().measurementId(),entity.getSeverityLevel().name());
    }
}
