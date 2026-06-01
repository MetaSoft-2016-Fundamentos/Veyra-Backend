package com.metasoft.veyra.platform.nursing.domain.model.entities;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.NursingHome;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Resident;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.RoomOccupancy;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.RoomStatus;
import com.metasoft.veyra.platform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Room entity.
 * <p>
 * This entity represents a room in a nursing home.
 * A room can be assigned to residents and has a capacity and occupancy.
 * </p>
 * @see NursingHome
 * @see Resident
 * @since 1.0
 */
@Getter
@Entity
public class Room extends AuditableModel {

    @ManyToOne
    @JoinColumn(name = "nursing_home_id")
    @NotNull
    private NursingHome nursingHome;

    @NotNull
    @Column(nullable = false)
    private String roomNumber;

    @NotNull
    @Column(nullable = false)
    private String type;

    @Embedded
    @NotNull
    private RoomOccupancy roomOccupancy;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private RoomStatus roomStatus;

    public Room() {
        super();
    }

    public Room(NursingHome nursingHome, Integer capacity, String type, String roomNumber) {
        this();
        this.nursingHome = nursingHome;
        this.type = type;
        this.roomOccupancy = new RoomOccupancy(capacity, 0);
        this.roomNumber = roomNumber;
        this.roomStatus = RoomStatus.AVAILABLE;
    }

    /**
     * Occupy a slot in the room.
     * Called when a resident is assigned to this room.
     */
    public void occupySlot() {
        if (this.roomOccupancy.isFull()) {
            throw new IllegalStateException("Room is at full capacity");
        }
        this.roomOccupancy = this.roomOccupancy.incrementOccupiedSlots(1);
        updateStatus();
    }

    /**
     * Release a slot in the room.
     * Called when a resident leaves this room.
     */
    public void releaseSlot() {
        if (this.roomOccupancy.occupied() == 0) {
            throw new IllegalStateException("No occupied slots to release");
        }
        this.roomOccupancy = this.roomOccupancy.release(1);
        updateStatus();
    }

    private void updateStatus() {
        if (this.roomOccupancy.occupied() == 0) {
            this.roomStatus = RoomStatus.AVAILABLE;
        } else if (this.roomOccupancy.isFull()) {
            this.roomStatus = RoomStatus.OCCUPIED;
        } else {
            this.roomStatus = RoomStatus.PARTIALLY_OCCUPIED;
        }
    }

    public boolean hasAvailableSlots() {
        return this.roomOccupancy.availableSlots() > 0;
    }

    public boolean isAvailable() {
        return hasAvailableSlots();
    }

    public Integer getOccupiedSlots() {
        return this.roomOccupancy.occupied();
    }

    public Integer getCapacity() {
        return this.roomOccupancy.capacity();
    }
}