package com.metasoft.veyra.platform.hcm.domain.services;

import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.hcm.domain.model.entities.Contract;
import com.metasoft.veyra.platform.hcm.domain.model.queries.*;
import com.metasoft.veyra.platform.hcm.domain.model.queries.GetStaffByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface StaffQueryServices {
    Optional<Staff>handle(GetStaffByIdQuery query);
    List<Staff>handle(GetAllStaffMemberByNursingHomeIdQuery query);
    Optional<Contract>handle(GetActiveContractByStaffMemberIdQuery query);
    List<Contract>handle(GetAllContractsByStaffMemberIdQuery query);
    Optional<Contract>handle(GetContractByStaffMemberIdAndContractIdQuery query);
    Optional<Contract>handle(GetLastAddedContractByStaffMemberIdQuery query);
    List<Staff>handle(GetAllActiveStaffByContractWithNurseRoleByNursingHomeIdQuery query);
    Optional<Staff> handle(GetStaffMemberWithNurseRoleAndActiveContractQuery query);
    Optional<Staff> handle(GetStaffByUserIdQuery query);
    Optional<Long> handle(GetNursingHomeByStaffIdQuery query); // Changed return type to Optional<Long>
}