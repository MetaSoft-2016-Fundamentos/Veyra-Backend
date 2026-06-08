package com.metasoft.veyra.platform.hcm.application.internal.queryservices;

import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.hcm.domain.model.entities.Contract;
import com.metasoft.veyra.platform.hcm.domain.model.queries.*;
import com.metasoft.veyra.platform.hcm.domain.services.StaffQueryServices;
import com.metasoft.veyra.platform.hcm.infrastructure.persistence.jpa.repositories.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class StaffQueryServiceImpl implements StaffQueryServices {
    private final StaffRepository staffRepository;

    public StaffQueryServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public Optional<Staff> handle(GetStaffByIdQuery query) {
      return staffRepository.findById(query.id());
    }

    @Override
    public List<Staff> handle(GetAllStaffMemberByNursingHomeIdQuery query) {
        return staffRepository.findByNursingHomeId(query.nursingHomeId());
    }

    @Override
    public Optional<Contract> handle(GetActiveContractByStaffMemberIdQuery query) {
        Staff staff = staffRepository.findById(query.staffId())
                .orElseThrow(() -> new IllegalArgumentException("Staff with id " + query.staffId() + " does not exist"));

        Long activeContractId = staff.getContractHistory().getActiveContractId();

        if (activeContractId == null) {
            return Optional.empty();
        }
        return staff.getContractHistory().getAllContracts().stream()
                .filter(contract -> contract.getId().equals(activeContractId))
                .findFirst();
    }

    @Override
    public List<Contract> handle(GetAllContractsByStaffMemberIdQuery query) {
        Staff staff = staffRepository.findById(query.staffId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Staff with id " + query.staffId() + " does not exist"
                ));

        return staff.getContractHistory().getAllContracts();
    }

    @Override
    public Optional<Contract> handle(GetContractByStaffMemberIdAndContractIdQuery query) {
        Staff staff = staffRepository.findById(query.staffMemberId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Staff with id " + query.staffMemberId() + " does not exist"
                ));

        return staff.getContractHistory().getContractById(query.contractId());
    }

    @Override
    public Optional<Contract> handle(GetLastAddedContractByStaffMemberIdQuery query) {
        return staffRepository.findById(query.staffId()).map(staff->staff.getContractHistory().getLastAddedContract());
    }

    @Override
    public List<Staff> handle(GetAllActiveStaffByContractWithNurseRoleByNursingHomeIdQuery query) {
        return staffRepository.findByNursingHomeId(query.nursingHomeId())
                .stream()
                .filter(staff -> "ACTIVE".equals(staff.getStaffStatus().name()))
                .filter(staff -> staff.getContractHistory().getAllContracts().stream()
                        .anyMatch(contract -> contract.getStaffRole() != null && 
                                             "NURSE".equals(contract.getStaffRole().name())))
                .toList();
    }

    @Override
    public Optional<Staff> handle(GetStaffMemberWithNurseRoleAndActiveContractQuery query) {
        return staffRepository.findById(query.staffId())
                .filter(staff -> staff.getNursingHomeId().equals(query.nursingHomeId()))
                .filter(staff -> "ACTIVE".equals(staff.getStaffStatus().name()))
                .filter(staff -> staff.getContractHistory().getAllContracts().stream()
                        .anyMatch(contract -> contract.getStaffRole() != null &&
                                "NURSE".equals(contract.getStaffRole().name()) &&
                                contract.isActive()))
                .or(Optional::empty);
    }
}
