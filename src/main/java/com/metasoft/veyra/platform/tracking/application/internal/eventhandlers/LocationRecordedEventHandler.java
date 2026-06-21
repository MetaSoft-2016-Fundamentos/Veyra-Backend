package com.metasoft.veyra.platform.tracking.application.internal.eventhandlers;
import com.metasoft.veyra.platform.tracking.domain.model.events.LocationRecordedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LocationRecordedEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationRecordedEventHandler.class);

    private final SimpMessagingTemplate messagingTemplate;

    public LocationRecordedEventHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void on(LocationRecordedEvent event) {
        var payload = Map.of(
                "deviceId", event.getDeviceId(),
                "latitude", event.getLatitude(),
                "longitude", event.getLongitude(),
                "recordedAt", event.getRecordedAt().toString()
        );
        var topic = "/topic/tracking/locations" + event.getDeviceId();
        messagingTemplate.convertAndSend(topic, payload);

        LOGGER.info("Broadcasted location to topic: {} | payload: {}", topic, payload);
    }
}