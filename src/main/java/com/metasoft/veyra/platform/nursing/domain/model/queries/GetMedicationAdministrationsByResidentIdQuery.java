package com.metasoft.veyra.platform.nursing.domain.model.queries;

public record GetMedicationAdministrationsByResidentIdQuery(Long residentId) {
    public GetMedicationAdministrationsByResidentIdQuery{
        if (residentId==null||residentId<1){
            throw new IllegalArgumentException("Resident id cannot be null or less 1");
        }
    }
}
