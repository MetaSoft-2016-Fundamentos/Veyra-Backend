package com.metasoft.veyra.platform.tracking.application.internal.eventhandlers;

import com.metasoft.veyra.platform.tracking.domain.model.events.MeasurementRecordedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MeasurementRecordedEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementRecordedEventHandler.class);
    private final SimpMessagingTemplate messagingTemplate;

    public MeasurementRecordedEventHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void on(MeasurementRecordedEvent event) {
        var payload = Map.of(
                "deviceId", event.getDeviceId(),
                "measurementTimestamp", event.getMeasurementTimestamp().toString(),
                "heartRate", event.getHeartRate(),
                "temperature", event.getTemperature(),
                "oxygenSaturation", event.getOxygenSaturation()
        );

        var topic = "/topic/tracking/measurements/" + event.getDeviceId();
        messagingTemplate.convertAndSend(topic, payload);

        LOGGER.info("Broadcasted measurement to topic: {} | payload: {}", topic, payload);
    }
}