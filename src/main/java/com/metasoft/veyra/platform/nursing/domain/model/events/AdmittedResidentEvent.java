/**
 * Domain event triggered when a new resident is successfully registered
 * in a nursing home within the system.
 *
 * <p>This event is published to notify interested listeners that a resident
 * has been added, providing contextual information such as the nursing home,
 * the resident's age, and the timestamp of registration.</p>
 *
 * @summary Event emitted upon registering a new resident with related metadata.
 */
package com.metasoft.veyra.platform.nursing.domain.model.events;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;
@Getter
public class AdmittedResidentEvent extends ApplicationEvent {
    private final Long residentId;
    private final Long nursingHomeId;
    private final LocalDate occurredOn;
  private final String residentState;
    public AdmittedResidentEvent(Object source, Long residentId, Long nursingHomeId, LocalDate occurredOn, String residentState) {
        super(source);
        this.residentId = residentId;
        this.nursingHomeId = nursingHomeId;
        this.occurredOn = occurredOn;
        this.residentState = residentState;
    }
}