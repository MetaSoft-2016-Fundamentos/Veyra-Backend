package com.metasoft.veyra.platform.health.application.internal.queryservices;

import com.metasoft.veyra.platform.health.domain.model.aggregates.Allergy;
import com.metasoft.veyra.platform.health.domain.model.queries.ExistsAllergyByResidentIdQuery;
import com.metasoft.veyra.platform.health.domain.model.queries.GetAllergiesByResidentIdQuery;
import com.metasoft.veyra.platform.health.domain.model.queries.GetAllergyByIdQuery;
import com.metasoft.veyra.platform.health.domain.services.AllergyQueryService;
import com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories.AllergyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AllergyQueryServiceImpl implements AllergyQueryService {
    private final AllergyRepository allergyRepository;

    public AllergyQueryServiceImpl(AllergyRepository allergyRepository) {
        this.allergyRepository = allergyRepository;
    }

    @Override
    public List<Allergy> handle(GetAllergiesByResidentIdQuery query) {
        return allergyRepository.findAllByResidentId(query.residentId());
    }

    @Override
    public boolean handle(ExistsAllergyByResidentIdQuery query) {
        return allergyRepository.existsByResidentId(query.residentId());
    }

    @Override
    public Optional<Allergy> handle(GetAllergyByIdQuery query) {
        return allergyRepository.findById(query.id());
    }
}
