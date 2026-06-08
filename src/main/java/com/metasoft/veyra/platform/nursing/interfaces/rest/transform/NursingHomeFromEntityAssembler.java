package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.NursingHome;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.NursingHomeResource;

public class NursingHomeFromEntityAssembler {
    public static NursingHomeResource toResourceFromEntity(NursingHome entity)
    {
        return new NursingHomeResource(entity.getId(),entity.getBusinessProfileId().businessProfileId(),entity.getAdministrator().getId());
    }
}
