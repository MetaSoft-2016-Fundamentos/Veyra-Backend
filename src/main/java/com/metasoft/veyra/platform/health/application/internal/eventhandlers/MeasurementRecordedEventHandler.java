package com.metasoft.veyra.platform.health.application.internal.eventhandlers;

import com.metasoft.veyra.platform.health.domain.model.commands.ValidateVitalSignCommand;
import com.metasoft.veyra.platform.health.domain.services.VitalSignCommandService;
import com.metasoft.veyra.platform.tracking.domain.model.events.MeasurementRecordedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service("MeasurementRecordedEventHandlerHealth")
public class MeasurementRecordedEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementRecordedEventHandler.class);

    private final VitalSignCommandService vitalSignCommandService;

    public MeasurementRecordedEventHandler(VitalSignCommandService vitalSignCommandService) {
        this.vitalSignCommandService = vitalSignCommandService;
    }

    @EventListener
    public void on(MeasurementRecordedEvent event) {
        LOGGER.debug("MeasurementRecordedEvent received for device {}", event.getDeviceId());
        var measurementId = event.getDeviceId() + "_" + event.getMeasurementTimestamp().toString();
        var command = new ValidateVitalSignCommand(
                measurementId,
                event.getDeviceId(),
                event.getHeartRate(),
                null,
                null,
                event.getTemperature(),
                event.getOxygenSaturation(),
                null
        );
        vitalSignCommandService.handle(command);
    }
}
