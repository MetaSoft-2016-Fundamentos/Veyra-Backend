package com.metasoft.veyra.platform.nursing.domain.services;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Medication;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetAllMedicationsByResidentIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetMedicationByIdQuery;

import java.util.List;
import java.util.Optional;

public interface MedicationQueryServices {
    Optional<Medication>handle(GetMedicationByIdQuery query);
    List<Medication>handle(GetAllMedicationsByResidentIdQuery query);
}
