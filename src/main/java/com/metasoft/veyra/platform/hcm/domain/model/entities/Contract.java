package com.metasoft.veyra.platform.hcm.domain.model.entities;

import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.*;
import com.metasoft.veyra.platform.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Contract extends AuditableModel {
@ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
@Embedded
    private ContractPeriod contractPeriod;
@Embedded
    private TypeOfContract typeOfContract;
@Enumerated(EnumType.STRING)
private ContractStatus contractStatus;
@Enumerated(EnumType.STRING)
private StaffRole staffRole;
@Enumerated(EnumType.STRING)
private WorkShift workShift;
public Contract(Staff staff,ContractPeriod contractPeriod,TypeOfContract typeOfContract,StaffRole staffRole,WorkShift workShift){
    this.staff = staff;
    this.contractPeriod =  contractPeriod;
    this.typeOfContract = typeOfContract;
    this.staffRole= staffRole;
    this.workShift= workShift;
    this.contractStatus=ContractStatus.ACTIVE;
    }
    public Contract(){this.contractStatus = ContractStatus.PENDING;}
    /**
     * Activate the contract
     * Can only activate from PENDING status
     */
    public void activate() {
        if (this.contractStatus == ContractStatus.ACTIVE) {
            throw new IllegalStateException("Contract is already active");
        }

        if (this.contractStatus == ContractStatus.TERMINATED) {
            throw new IllegalStateException(
                    "Cannot activate a terminated contract. Terminated contracts are final."
            );
        }

        if (this.contractStatus == ContractStatus.SUSPENDED) {
            throw new IllegalStateException(
                    "Cannot activate a suspended contract. Suspended contracts are final."
            );
        }

        this.contractStatus = ContractStatus.ACTIVE;
    }

    /**
     * Terminate the contract
     * Can only terminate from ACTIVE status
     * TERMINATED is a final state - cannot be changed
     */
    public void terminate() {
        if (this.contractStatus == ContractStatus.TERMINATED) {
            throw new IllegalStateException("Contract is already terminated");
        }

        if (this.contractStatus == ContractStatus.SUSPENDED) {
            throw new IllegalStateException(
                    "Cannot terminate a suspended contract. Suspended contracts are final."
            );
        }

        if (this.contractStatus != ContractStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Can only terminate an active contract. Current status: " + this.contractStatus
            );
        }

        this.contractStatus = ContractStatus.TERMINATED;
    }

    /**
     * Suspend the contract
     * Can only suspend from ACTIVE status
     * SUSPENDED is a final state - cannot be changed
     */
    public void suspend() {
        if (this.contractStatus == ContractStatus.SUSPENDED) {
            throw new IllegalStateException("Contract is already suspended");
        }

        if (this.contractStatus == ContractStatus.TERMINATED) {
            throw new IllegalStateException(
                    "Cannot suspend a terminated contract. Terminated contracts are final."
            );
        }

        if (this.contractStatus != ContractStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Can only suspend an active contract. Current status: " + this.contractStatus
            );
        }

        this.contractStatus = ContractStatus.SUSPENDED;
    }

    /**
     * Update status with validation
     * Enforces business rules for state transitions
     */
    public void updateStatus(ContractStatus newStatus) {
        if (this.contractStatus == newStatus) {
            throw new IllegalStateException(
                    "Contract is already in " + newStatus + " status"
            );
        }

        // Validate state transitions
        switch (newStatus) {
            case ACTIVE:
                activate();
                break;
            case TERMINATED:
                terminate();
                break;
            case SUSPENDED:
                suspend();
                break;
            case PENDING:
                throw new IllegalStateException(
                        "Cannot change contract back to PENDING status"
                );
            default:
                throw new IllegalStateException("Invalid contract status: " + newStatus);
        }
    }

    // ============================================
    // STATUS QUERY METHODS
    // ============================================

    public boolean isActive() {
        return this.contractStatus == ContractStatus.ACTIVE;
    }


    /**
     * Check if the contract is in a final state (cannot be changed)
     */
    public boolean isFinalState() {
        return this.contractStatus == ContractStatus.TERMINATED
                || this.contractStatus == ContractStatus.SUSPENDED;
    }


}
