/**
 * Domain event triggered when a medication's stock reaches a low or critical level.
 *
 * <p>This event is published within the application context to notify listeners
 * or handlers that a specific medication, associated with a resident, requires
 * attention due to insufficient stock.</p>
 *
 * @summary Event emitted when medication stock becomes low, carrying medication and resident details.
 */
package com.metasoft.veyra.platform.nursing.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MedicationStockLowEvent extends ApplicationEvent {
    private final Long medicationId;
    private final String name;
    private final Long residentId;

    public MedicationStockLowEvent(Object source, Long medicationId, String name, Long residentId){
        super(source);
        this.medicationId = medicationId;
        this.name = name;
        this.residentId = residentId;
    }
}
