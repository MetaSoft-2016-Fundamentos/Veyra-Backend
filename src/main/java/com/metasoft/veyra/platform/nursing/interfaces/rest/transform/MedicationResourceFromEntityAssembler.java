package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Medication;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.MedicationResource;

public class MedicationResourceFromEntityAssembler {
    public static MedicationResource toResourceFromEntity(Medication entity)
    {
        return new MedicationResource(entity.getId(),entity.getResident().getId(),entity.getName(),entity.getDescription()
        ,entity.getStock().amount(), entity.getExpirationDate().expirationDate(),entity.getDrugPresentation().name(),entity.getDosage());
    }
}