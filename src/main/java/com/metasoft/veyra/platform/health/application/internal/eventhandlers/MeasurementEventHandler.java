package com.metasoft.veyra.platform.health.application.internal.eventhandlers;

import com.metasoft.veyra.platform.health.domain.model.commands.ValidateVitalSignCommand;
import com.metasoft.veyra.platform.health.domain.services.VitalSignCommandService;
import com.metasoft.veyra.platform.tracking.domain.model.events.MeasurementReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class MeasurementEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementEventHandler.class);

    private final VitalSignCommandService vitalSignCommandService;

    public MeasurementEventHandler(VitalSignCommandService vitalSignCommandService) {
        this.vitalSignCommandService = vitalSignCommandService;
    }

    @EventListener
    public void on(MeasurementReceivedEvent event) {
        LOGGER.debug("Received measurement from device {} (ID: {})",
                event.getDeviceId(), event.getMeasurementId());

        var command = new ValidateVitalSignCommand(
                event.getMeasurementId(),
                event.getDeviceId(),
                event.getHeartRate(),
                event.getSystolic(),
                event.getDiastolic(),
                event.getTemperature(),
                event.getOxygenSaturation(),
                event.getRespiratoryRate()
        );

        vitalSignCommandService.handle(command);
    }
}
