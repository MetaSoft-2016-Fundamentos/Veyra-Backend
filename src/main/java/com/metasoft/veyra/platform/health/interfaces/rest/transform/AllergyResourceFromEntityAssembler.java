package com.metasoft.veyra.platform.health.interfaces.rest.transform;

import com.metasoft.veyra.platform.health.domain.model.aggregates.Allergy;
import com.metasoft.veyra.platform.health.interfaces.rest.resources.AllergyResource;

public class AllergyResourceFromEntityAssembler {
    public static AllergyResource toResourceFromEntity(Allergy entity){
     return new AllergyResource
     (entity.getId(),entity.getResidentId().residentId(),entity.getAllergenName(), entity.getReaction(),entity.getSeverityLevel().name(),entity.getTypeOfAllergy().name());
    }
}
