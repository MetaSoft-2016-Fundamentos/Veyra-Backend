package com.metasoft.veyra.platform.hcm.domain.model.aggregates;

import com.metasoft.veyra.platform.hcm.domain.model.events.EmployeeHiredEvent;
import com.metasoft.veyra.platform.hcm.domain.model.events.EmployeeSuspendedEvent;
import com.metasoft.veyra.platform.hcm.domain.model.events.EmployeeTerminationEvent;
import com.metasoft.veyra.platform.hcm.domain.model.events.RegisteredStaffEvent;
import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.*;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Staff extends AuditableAbstractAggregateRoot<Staff> {
    protected Staff() {

        super();
        contractHistory = new ContractHistory();
    }

    @Embedded
    private UserId userId;

    @Embedded
    private final ContractHistory contractHistory;

    @Embedded
    private EmergencyContact emergencyContact;
    @Embedded
    private NursingHomeId nursingHomeId;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "person_profile_id"))
    private PersonProfileId personProfileId;

    @Enumerated(EnumType.STRING)
    private StaffStatus staffStatus;

    public Staff(Long personProfileId, String emergencyContactFirstName, String emergencyContactLastName, String emergencyContactPhoneNumber) {
        this();
        this.personProfileId = new PersonProfileId(personProfileId);
        this.emergencyContact = new EmergencyContact(emergencyContactFirstName, emergencyContactLastName, emergencyContactPhoneNumber);
    }

    public Staff(PersonProfileId personProfileId, NursingHomeId nursingHomeId, EmergencyContact emergencyContact) {
        this();
        this.personProfileId = personProfileId;
        this.emergencyContact = emergencyContact;
        this.nursingHomeId = nursingHomeId;
        this.staffStatus = StaffStatus.INACTIVE;
    }

    public Staff updateEmergencyContact(String emergencyContactFirstName, String emergencyContactLastName, String emergencyContactPhoneNumber) {
        this.emergencyContact = new EmergencyContact(emergencyContactFirstName, emergencyContactLastName, emergencyContactPhoneNumber);
        return this;
    }

    public void addContractToHistory(LocalDate startDate, LocalDate endDate,
                                     String typeOfContract, String staffRole, String workShift) {
        this.contractHistory.addContract(this, startDate, endDate, typeOfContract, staffRole, workShift);
        var aux = this.contractHistory.getLastAddedContract();
        this.addDomainEvent(new EmployeeHiredEvent(this, this.getId(), aux.getId(), this.nursingHomeId, staffRole, typeOfContract, LocalDate.now()));
        this.staffStatus = StaffStatus.ACTIVE;
    }

    public void updateContractStatus(Long contractId, String newStatus) {
        this.contractHistory.updateContractStatus(contractId, newStatus);
        var contractOptional = this.contractHistory.getContractById(contractId);
        var contract = contractOptional.orElseThrow(() -> new IllegalArgumentException(
                "Contract with id " + contractId + " not found in this staff's history"
        ));
        String staffRole = contract.getStaffRole() != null ? contract.getStaffRole().name() : null;
        String typeOfContract = contract.getTypeOfContract() != null ? contract.getTypeOfContract().typeOfContract() : null;
        if (Objects.equals(newStatus, "TERMINATED")) {
            this.addDomainEvent(new EmployeeTerminationEvent(this, getId(), contractId, this.nursingHomeId, staffRole, typeOfContract, LocalDate.now()));
            this.staffStatus = StaffStatus.INACTIVE;
        } else if (Objects.equals(newStatus, "SUSPENDED")) {
            this.addDomainEvent(new EmployeeSuspendedEvent(this, this.getId(), contractId, this.nursingHomeId, staffRole, typeOfContract, LocalDate.now()));
            this.staffStatus = StaffStatus.SUSPENDED;
        }
    }


}
