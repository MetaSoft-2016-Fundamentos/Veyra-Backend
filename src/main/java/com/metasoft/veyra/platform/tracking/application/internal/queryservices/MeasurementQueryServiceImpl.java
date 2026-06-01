package com.metasoft.veyra.platform.tracking.application.internal.queryservices;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Measurement;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetAllMeasurementQuery;
import com.metasoft.veyra.platform.tracking.domain.services.MeasurementQueryService;
import com.metasoft.veyra.platform.tracking.infrastructure.persistence.mongodb.repositories.MeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasurementQueryServiceImpl implements MeasurementQueryService {
private final MeasurementRepository measurementRepository;

    public MeasurementQueryServiceImpl(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    @Override
    public List<Measurement> handle(GetAllMeasurementQuery query) {
        return measurementRepository.findAll();
    }
}
