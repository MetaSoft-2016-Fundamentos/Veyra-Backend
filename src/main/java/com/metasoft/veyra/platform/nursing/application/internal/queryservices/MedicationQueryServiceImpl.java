package com.metasoft.veyra.platform.nursing.application.internal.queryservices;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Medication;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetAllMedicationsByResidentIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetMedicationByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.services.MedicationQueryServices;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.MedicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationQueryServiceImpl implements MedicationQueryServices {
    private final MedicationRepository medicationRepository;

    public MedicationQueryServiceImpl(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    public Optional<Medication> handle(GetMedicationByIdQuery query) {
        return medicationRepository.findById(query.id());
    }

    @Override
    public List<Medication> handle(GetAllMedicationsByResidentIdQuery query) {
        return medicationRepository.findByResidentId(query.id());
    }


}
