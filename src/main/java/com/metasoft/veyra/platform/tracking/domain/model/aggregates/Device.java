package com.metasoft.veyra.platform.tracking.domain.model.aggregates;

import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Device extends AuditableAbstractAggregateRoot<Device> {
    @Embedded
    private MacAddress macAddress;
@Embedded
    private ResidentId residentId;
@Embedded
    private NursingHomeId nursingHomeId;

    @Column(nullable = true)
    private LocalDateTime assignedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AssignmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DeviceType deviceType;

    public Device() {}

    public Device(Long nursingHomeId, String deviceType, String macAddress) {
        this.macAddress = new MacAddress(macAddress);
        this.nursingHomeId =new NursingHomeId( nursingHomeId);
        this.deviceType = DeviceType.valueOf(deviceType.toUpperCase());
        this.assignedAt = null;
        this.status = AssignmentStatus.AVAILABLE;
    }

    public void assignToResident(Long residentId) {
        if (this.status!=AssignmentStatus.AVAILABLE){
            throw new IllegalArgumentException("Only available devices can be assigned to a resident");

        }
        this.residentId = new ResidentId(residentId);
        this.assignedAt = LocalDateTime.now();
        this.status = AssignmentStatus.ASSIGNED;
    }

    public void unassign() {
        if (this.status != AssignmentStatus.ASSIGNED) {
            throw new IllegalStateException(
                    "Only assigned devices can be unassigned");
        }

        this.residentId = null;
        this.status = AssignmentStatus.AVAILABLE;
        this.assignedAt=null;
    }

    public void updateDevice(String deviceType,String macAddress) {
        this.deviceType = DeviceType.valueOf(deviceType.toUpperCase());
        this.macAddress= new MacAddress(macAddress);
    }

    public void changeStatus(String newStatus) {

        var status = AssignmentStatus.valueOf(newStatus.toUpperCase());

        if (this.status == AssignmentStatus.ASSIGNED) {
            throw new IllegalArgumentException(
                    "Assigned devices must be unassigned before changing status");
        }

        if (status == AssignmentStatus.ASSIGNED) {
            throw new IllegalArgumentException(
                    "Use assignToResident() to assign a device");
        }
        if (this.status == status) {
            throw new IllegalArgumentException(
                    "Device already has status " + status);
        }
        this.status = status;
    }

    public boolean isAssigned() {
        return this.residentId != null
                && this.status == AssignmentStatus.ASSIGNED;
    }
}