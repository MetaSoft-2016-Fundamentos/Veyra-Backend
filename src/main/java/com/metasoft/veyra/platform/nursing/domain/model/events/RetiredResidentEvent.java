package com.metasoft.veyra.platform.nursing.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

@Getter
public class RetiredResidentEvent extends ApplicationEvent {
    private final Long nursingHomeId;
    private final LocalDate occurredOn;
    private final String residentState;
    private final Long residentId;
    public RetiredResidentEvent(Object source, Long nursingHomeId, LocalDate occurredOn, String residentState, Long residentId) {
        super(source);
        this.nursingHomeId = nursingHomeId;
        this.occurredOn = occurredOn;
        this.residentState = residentState;
        this.residentId = residentId;
    }
}
