/**
 * Aggregate root representing a nursing home within the system.
 *
 * <p>This entity links to a business profile through its {@link BusinessProfileId}
 * value object, and inherits auditing and domain event capabilities from
 * {@link AuditableAbstractAggregateRoot}.</p>
 *
 * @summary Represents a nursing home aggregate with an embedded business profile identifier.
 */
package com.metasoft.veyra.platform.nursing.domain.model.aggregates;

import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.BusinessProfileId;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.Rooms;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class NursingHome extends AuditableAbstractAggregateRoot<NursingHome> {
    @JoinColumn(name = "administrator_Id")
    @ManyToOne
    private Administrator administrator;
    @Embedded
    private BusinessProfileId businessProfileId;

    public NursingHome() {}

    public NursingHome(BusinessProfileId businessProfileId, Administrator administrator) {
        this.businessProfileId = businessProfileId;
        this.administrator=administrator;
    }
    @Embedded
    private Rooms rooms;
    /**
     * Add a room to the nursing home.
     * @param capacity the capacity of the room
     * @param type the type of the room
     */
    public void addRoom(Integer capacity, String type,String roomNumber) {
        this.rooms.addRoom(this, capacity, type,roomNumber);
    }
}
