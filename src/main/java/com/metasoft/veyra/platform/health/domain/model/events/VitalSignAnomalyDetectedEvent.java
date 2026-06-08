package com.metasoft.veyra.platform.health.domain.model.events;

import com.metasoft.veyra.platform.health.domain.model.valueobjects.SeverityLevel;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class VitalSignAnomalyDetectedEvent extends ApplicationEvent {

    private final Long vitalSignId;
    private final Long residentId;
    private final String  measurementId;
    private final SeverityLevel severity;
    private final String details;
    private final LocalDateTime detectedAt;

    public VitalSignAnomalyDetectedEvent(
            Object source,
            Long vitalSignId,
            Long residentId,
            String measurementId,
            SeverityLevel severity,
            String details) {

        super(source);
        this.vitalSignId = vitalSignId;
        this.residentId = residentId;
        this.measurementId = measurementId;
        this.severity = severity;
        this.details = details;
        this.detectedAt = LocalDateTime.now();
    }
}