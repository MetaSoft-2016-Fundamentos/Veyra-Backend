package com.metasoft.veyra.platform.nursing.domain.model.aggregates;
import com.metasoft.veyra.platform.nursing.domain.model.entities.Room;
import com.metasoft.veyra.platform.nursing.domain.model.events.AdmittedResidentEvent;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.*;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Resident extends AuditableAbstractAggregateRoot<Resident> {
    @Embedded
    private PersonProfileId personProfileId;
    @Embedded
    @AttributeOverrides(
            value = {
                    @AttributeOverride(name = "firstName", column = @Column(name = "emergency_firstname")),
                    @AttributeOverride(name = "lastName", column = @Column(name = "emergency_lastname")),
                    @AttributeOverride(name = "phoneNumber", column = @Column(name = "emergency_phone_number"))
            }
    )
    private EmergencyContact emergencyContact;
    @Embedded
    @AttributeOverrides(
            value = {
                    @AttributeOverride(name = "firstName", column = @Column(name = "legal_representative_firstname")),
                    @AttributeOverride(name = "lastName", column = @Column(name = "legal_representative_lastname")),
                    @AttributeOverride(name = "phoneNumber", column = @Column(name = "legal_representative_phone_number"))
            }
    )
    private LegalRepresentative legalRepresentative;
    @ManyToOne
    @JoinColumn(name = "nursing_home_id")
    private NursingHome nursingHome;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ResidentState residentStatus;
    @Embedded
    private StaffMemberId staffMemberId;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Resident(Long personProfileId, String legalRepresentativeFirstName, String legalRepresentativeLastName, String legalRepresentativePhoneNumber
            , String emergencyContactFirstName, String emergencyContactLastName, String emergencyContactPhoneNumber) {
        this.personProfileId = new PersonProfileId(personProfileId);
        this.legalRepresentative = new LegalRepresentative(legalRepresentativeFirstName, legalRepresentativeLastName, legalRepresentativePhoneNumber);
        this.emergencyContact = new EmergencyContact(emergencyContactFirstName, emergencyContactLastName, emergencyContactPhoneNumber);
    }

    public Resident(NursingHome nursingHome, PersonProfileId personProfileId, LegalRepresentative legalRepresentative, EmergencyContact emergencyContact) {

        this.personProfileId = personProfileId;
        this.legalRepresentative = legalRepresentative;
        this.emergencyContact = emergencyContact;
        this.nursingHome = nursingHome;
        this.residentStatus = ResidentState.ACTIVE;
       this.addDomainEvent(new AdmittedResidentEvent(this,this.getId(),nursingHome.getId(), LocalDate.now(),residentStatus.name()));

    }
    /**
     * Assign this resident to a room.
     * @param room the room to assign
     * @throws IllegalStateException if resident is not active or already assigned
     */
    public void assignToRoom(Room room) {
        if (!this.isActive()) {
            throw new IllegalStateException("Cannot assign inactive resident to a room");
        }
        if (this.room != null) {
            throw new IllegalStateException("Resident is already assigned to room: " + this.room.getRoomNumber());
        }
        if (!room.hasAvailableSlots()) {
            throw new IllegalStateException("Room " + room.getRoomNumber() + " has no available slots");
        }

        this.room = room;
        room.occupySlot();
    }

    /**
     * Change resident to a different room.
     * @param newRoom the new room
     * @throws IllegalStateException if not currently assigned or new room is full
     */
    public void changeRoom(Room newRoom) {
        if (this.room == null) {
            throw new IllegalStateException("Resident is not currently assigned to any room");
        }
        if (!newRoom.hasAvailableSlots()) {
            throw new IllegalStateException("Room " + newRoom.getRoomNumber() + " has no available slots");
        }
        if (this.room.equals(newRoom)) {
            throw new IllegalStateException("Resident is already in this room");
        }

        Room oldRoom = this.room;
        this.room = newRoom;

        oldRoom.releaseSlot();
        newRoom.occupySlot();
    }

    /**
     * Remove resident from their current room.
     * Should be called when resident retires or is deceased.
     */
    public void leaveRoom() {
        if (this.room == null) {
            throw new IllegalStateException("Resident is not assigned to any room");
        }

        this.room.releaseSlot();
        this.room = null;
    }

public Resident(){}

    public void activate() {
        if (this.residentStatus == ResidentState.ACTIVE) {
            throw new IllegalStateException("Resident is already exists");
        }
        if (this.residentStatus == ResidentState.RETIRED) {
            throw new IllegalStateException("A voluntary withdrawal cannot be reactivated. Voluntary withdrawals are final.");
        }
        if (this.residentStatus == ResidentState.DECEASED) {
            throw new IllegalStateException("The status of a deceased person cannot be reactivated.");

        }
        this.residentStatus = ResidentState.ACTIVE;
    }

    public void retired() {
        if (this.residentStatus == ResidentState.RETIRED) {
            throw new IllegalStateException("Resident already retired");
        }
        if (this.residentStatus == ResidentState.DECEASED) {
            throw new IllegalStateException("A deceased resident cannot be retired");
        }
        if (this.residentStatus != ResidentState.ACTIVE) {
            throw new IllegalStateException(
                    "Can only suspend an active resident. Current status: " + this.residentStatus
            );
        }
        if (this.room != null) {
            this.leaveRoom();
        }
        this.residentStatus = ResidentState.RETIRED;
    }

    public void deceased() {
        if (this.residentStatus == ResidentState.DECEASED) {
            throw new IllegalStateException("A deceased resident cannot be retired");
        }
        if (this.residentStatus == ResidentState.RETIRED) {
            throw new IllegalStateException("Resident already retired");
        }
        if (this.residentStatus != ResidentState.ACTIVE) {
            throw new IllegalStateException(
                    "Can only suspend an active resident. Current status: " + this.residentStatus
            );
        }
        if (this.room != null) {
            this.leaveRoom();
        }
        this.residentStatus = ResidentState.DECEASED;
    }

    public void updateStatus(ResidentState newStatus) {
        switch (newStatus) {
            case ACTIVE:
                activate();
                break;
            case RETIRED:
                retired();
                break;
            case DECEASED:
                deceased();
                break;
            default:
                throw new IllegalStateException("Invalid resident state");
        }
    }

    public boolean isActive() {
        return this.residentStatus == ResidentState.ACTIVE;
    }

    public boolean isFinalState() {
        return this.residentStatus == ResidentState.RETIRED || this.residentStatus == ResidentState.DECEASED;
    }

    public Resident updateLegalRepresentative(String firstName, String lastName, String phoneNumber) {
        this.legalRepresentative = new LegalRepresentative(firstName, lastName, phoneNumber);
        return this;
    }

    public Resident updateEmergencyContact(String firstName, String lastName, String phoneNumber) {
        this.emergencyContact = new EmergencyContact(firstName, lastName, phoneNumber);
        return this;
    }

    public void assignedStaffToResidentCommand(Long staffMemberId){
        this.staffMemberId=new StaffMemberId(staffMemberId);
    }
}