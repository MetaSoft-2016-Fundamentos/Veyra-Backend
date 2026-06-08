package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateMedicationCommand;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateMedicationResource;

public class CreateMedicationCommandFromResourceAssembler {
    public static CreateMedicationCommand toCommandFromResource(CreateMedicationResource resource,Long residentId)
    {
        return new CreateMedicationCommand
                (resource.name(),resource.description(),resource.amount(),resource.expirationDate(),resource.drugPresentation(),resource.dosage(),residentId);
    }
}
