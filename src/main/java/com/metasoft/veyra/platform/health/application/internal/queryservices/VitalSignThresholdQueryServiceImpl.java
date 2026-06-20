package com.metasoft.veyra.platform.health.application.internal.queryservices;

import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSignThreshold;
import com.metasoft.veyra.platform.health.domain.model.queries.GetVitalSignThresholdByResidentIdQuery;
import com.metasoft.veyra.platform.health.domain.services.VitalSignThresholdQueryService;
import com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories.VitalSignThresholdRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VitalSignThresholdQueryServiceImpl implements VitalSignThresholdQueryService {

    private final VitalSignThresholdRepository repository;

    public VitalSignThresholdQueryServiceImpl(VitalSignThresholdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<VitalSignThreshold> handle(GetVitalSignThresholdByResidentIdQuery query) {
        return repository.findByResidentId(query.residentId());
    }
}
