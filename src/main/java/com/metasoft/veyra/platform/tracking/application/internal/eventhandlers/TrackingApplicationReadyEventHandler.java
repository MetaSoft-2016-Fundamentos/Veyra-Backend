package com.metasoft.veyra.platform.tracking.application.internal.eventhandlers;

import com.metasoft.veyra.platform.tracking.domain.model.commands.SeedDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.SeedMeasurementCommand;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceCommandService;
import com.metasoft.veyra.platform.tracking.domain.services.MeasurementCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class TrackingApplicationReadyEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingApplicationReadyEventHandler.class);

    private final DeviceCommandService deviceCommandService;
    private final MeasurementCommandService measurementCommandService;

    public TrackingApplicationReadyEventHandler(
            DeviceCommandService deviceCommandService,
            MeasurementCommandService measurementCommandService) {
        this.deviceCommandService = deviceCommandService;
        this.measurementCommandService = measurementCommandService;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        var applicationName = event.getApplicationContext().getId();
        LOGGER.info("Starting seeding process for {} at {}", applicationName, currentTimestamp());

        LOGGER.info("Step 1: Seeding devices...");
        var seedDeviceCommand = new SeedDeviceCommand();
        deviceCommandService.handle(seedDeviceCommand);

        LOGGER.info("Step 2: Seeding measurements...");
        var seedMeasurementCommand = new SeedMeasurementCommand();
        measurementCommandService.handle(seedMeasurementCommand);

        LOGGER.info("Seeding process finished for {} at {}", applicationName, currentTimestamp());
    }

    private Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}