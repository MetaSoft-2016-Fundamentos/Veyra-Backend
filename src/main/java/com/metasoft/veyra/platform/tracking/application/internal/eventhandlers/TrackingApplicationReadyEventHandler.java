package com.metasoft.veyra.platform.tracking.application.internal.eventhandlers;

import com.metasoft.veyra.platform.tracking.domain.model.commands.SeedMeasurementCommand;
import com.metasoft.veyra.platform.tracking.domain.services.MeasurementCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class TrackingApplicationReadyEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingApplicationReadyEventHandler.class);

    private final MeasurementCommandService measurementCommandService;
    private final boolean measurementSeedingEnabled;

    public TrackingApplicationReadyEventHandler(
            MeasurementCommandService measurementCommandService,
            @Value("${tracking.seeding.measurements.enabled:false}") boolean measurementSeedingEnabled
    ) {

        this.measurementCommandService = measurementCommandService;
        this.measurementSeedingEnabled = measurementSeedingEnabled;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getId();
        LOGGER.info("Starting tracking seeding for {} at {}", applicationName, currentTimestamp());


        if (measurementSeedingEnabled) {
            LOGGER.info("Step 2: Seeding measurements...");
        } else {
            LOGGER.info("Step 2: Measurement seeding disabled. Skipping.");
        }

        LOGGER.info("Tracking seeding finished for {} at {}", applicationName, currentTimestamp());
    }

    private Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
