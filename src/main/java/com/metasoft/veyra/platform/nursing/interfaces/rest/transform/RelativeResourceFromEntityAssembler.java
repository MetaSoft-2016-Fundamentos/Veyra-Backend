package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Relative;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.RelativeResource;

public class RelativeResourceFromEntityAssembler {
    public static RelativeResource toResourceFromEntity(Relative entity) {
        return new RelativeResource(entity.getId(),entity.getUserId().userId());
    }
}
