package com.metasoft.veyra.platform.tracking.application.internal.commandservices;


import com.metasoft.veyra.platform.tracking.domain.services.MeasurementCommandService;
import com.metasoft.veyra.platform.tracking.infrastructure.persistence.mongodb.repositories.MeasurementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;


@Service
public class MeasurementCommandServiceImpl implements MeasurementCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementCommandServiceImpl.class);

    private final ApplicationEventPublisher eventPublisher;
    private final MeasurementRepository measurementRepository;

    public MeasurementCommandServiceImpl(
            ApplicationEventPublisher eventPublisher,
            MeasurementRepository measurementRepository) {
        this.eventPublisher = eventPublisher;
        this.measurementRepository = measurementRepository;
    }

}