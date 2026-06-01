package com.metasoft.veyra.platform.hcm.domain.model.valueobjects;

import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.hcm.domain.model.entities.Contract;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Embeddable
public class ContractHistory {

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts;

    public ContractHistory() {
        this.contracts = new ArrayList<>();
    }

    /**
     * Add a new contract to the history
     */
    public void addContract(Staff staff, LocalDate startDate, LocalDate endDate,
                            String typeOfContract, String staffRole, String workShift) {
        if (hasActiveContract()) {
            throw new IllegalStateException(
                    "Cannot add new contract. There is already an active contract. " +
                            "Please terminate or suspend it first."
            );
        }

        ContractPeriod contractPeriod = new ContractPeriod(startDate, endDate);
        TypeOfContract contractType = new TypeOfContract(typeOfContract);
        StaffRole role = StaffRole.valueOf(staffRole);
        WorkShift shift = WorkShift.valueOf(workShift);

        Contract contract = new Contract(staff, contractPeriod, contractType, role, shift);
        contracts.add(contract);
    }


    public void updateContractStatus(Long contractId, String newStatus) {
        if (contractId == null) {
            throw new IllegalArgumentException("Contract id cannot be null");
        }

        Contract contract = contracts.stream()
                .filter(c -> contractId.equals(c.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Contract with id " + contractId + " not found in this staff's history"
                ));

        if (contract.isFinalState()) {
            throw new IllegalStateException(
                    String.format("Cannot update contract status. Contract is in final state: %s",
                            contract.getContractStatus())
            );
        }
        ContractStatus contractStatus = ContractStatus.valueOf(newStatus);
        contract.updateStatus(contractStatus);
    }

    private Contract getActiveContract() {
        return contracts.stream()
                .filter(Contract::isActive)
                .findFirst()
                .orElse(null);
    }

    public boolean hasActiveContract() {
        return getActiveContract() != null;
    }

    public boolean isEmpty() {
        return contracts.isEmpty();
    }

    public List<Contract> getAllContracts() {
        return new ArrayList<>(contracts);
    }

    public Long getActiveContractId() {
        return contracts.stream()
                .filter(Contract::isActive)
                .findFirst()
                .map(Contract::getId)
                .orElse(null);
    }

    public Optional<Contract> getContractById(Long contractId) {
        return contracts.stream()
                .filter(c -> c.getId().equals(contractId))
                .findFirst();

    }
    /**
     * Get the last added contract.
     * @return the last contract in the list, or null if empty
     */
    public Contract getLastAddedContract() {
        if (contracts.isEmpty()) {
            return null;
        }
        return contracts.getLast();
    }
}