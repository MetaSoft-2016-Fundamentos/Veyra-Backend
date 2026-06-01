package com.metasoft.veyra.platform.health.interfaces.rest.transform;

import com.metasoft.veyra.platform.health.domain.model.commands.RegisterAllergyCommand;
import com.metasoft.veyra.platform.health.interfaces.rest.resources.RegisterAllergyResource;

public class RegisterAllergyCommandFromResourceAssembler {
    public static RegisterAllergyCommand toCommandFromResource(RegisterAllergyResource resource, Long residentId){
        return new RegisterAllergyCommand(residentId,resource.reaction(),resource.allergenName(),resource.typeOfAllergy(),resource.severityLevel());
    }
}
