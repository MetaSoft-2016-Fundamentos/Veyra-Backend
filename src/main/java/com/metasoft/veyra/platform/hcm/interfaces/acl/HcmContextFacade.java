package com.metasoft.veyra.platform.hcm.interfaces.acl;

import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;

import java.util.List;
import java.util.Optional;

public interface HcmContextFacade {
    List<Staff> getAllActiveStaffWithNurseRoleByNursingHomeId(NursingHomeId nursingHomeId);
    Long getStaffById(Long id);
    List<Staff> getAllStaffByNursingHomeId(NursingHomeId nursingHomeId);
    List<Staff> getAllActiveStaffByNursingHomeId(NursingHomeId nursingHomeId);
    Optional<Staff> getStaffMemberWithNurseRoleAndActiveContract(Long staffId, NursingHomeId nursingHomeId);
    Long getStaffByUserId(Long userId);

    Long fetchUserIdByStaffId(Long staffId);
}