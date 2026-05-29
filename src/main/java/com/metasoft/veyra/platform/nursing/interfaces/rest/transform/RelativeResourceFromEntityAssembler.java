package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Relative;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.RelativeResource;

public class RelativeResourceFromEntityAssembler {
    public static RelativeResource toResourceFromEntity(Relative entity) {
        var userId= entity.getUserId()!=null ? entity.getUserId().userId(): null;
        return new RelativeResource(entity.getId(),entity.getEmailAddress().emailAddress(),entity.getPersonName().firstName(),entity.getPersonName().lastName(),userId);
    }
}
