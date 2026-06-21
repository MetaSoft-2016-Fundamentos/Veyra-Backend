package com.metasoft.veyra.platform.health.application.internal.eventhandlers;

import com.metasoft.veyra.platform.health.application.internal.outboundservices.acl.ExternalCommunicationService;
import com.metasoft.veyra.platform.health.domain.model.events.VitalSignAnomalyDetectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class VitalSignAnomalyDetectedEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(VitalSignAnomalyDetectedEventHandler.class);

    private final ExternalCommunicationService externalCommunicationService;

    public VitalSignAnomalyDetectedEventHandler(ExternalCommunicationService externalCommunicationService) {
        this.externalCommunicationService = externalCommunicationService;
    }

    @EventListener
    public void on(VitalSignAnomalyDetectedEvent event) {
        LOGGER.info("VitalSignAnomalyDetected for resident {} with severity {}", event.getResidentId(), event.getSeverity());
        externalCommunicationService.sendAnomalyAlert(
                event.getResidentId(),
                event.getSeverity().name(),
                event.getDetails()
        );
    }
}
