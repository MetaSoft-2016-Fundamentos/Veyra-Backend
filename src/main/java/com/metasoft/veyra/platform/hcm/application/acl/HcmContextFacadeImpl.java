package com.metasoft.veyra.platform.hcm.application.acl;

import com.metasoft.veyra.platform.hcm.domain.model.queries.*;
import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.hcm.domain.services.StaffQueryServices;
import com.metasoft.veyra.platform.hcm.interfaces.acl.HcmContextFacade;
import com.metasoft.veyra.platform.hcm.domain.model.queries.GetStaffByUserIdQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HcmContextFacadeImpl implements HcmContextFacade {
    private final StaffQueryServices staffQueryServices;

    public HcmContextFacadeImpl(StaffQueryServices staffQueryServices) {
        this.staffQueryServices = staffQueryServices;
    }


    @Override
    public List<Staff> getAllActiveStaffWithNurseRoleByNursingHomeId(NursingHomeId nursingHomeId) {
        var getAllActiveStaffWithNurseRoleByNursingHomeIdQuery = new GetAllActiveStaffByContractWithNurseRoleByNursingHomeIdQuery(nursingHomeId);
        var result = staffQueryServices.handle(getAllActiveStaffWithNurseRoleByNursingHomeIdQuery);
        return result.stream().toList();
    }

    @Override
    public Long getStaffById(Long id) {
        var getStaffByIdQuery = new GetStaffByIdQuery(id);
        var result = staffQueryServices.handle(getStaffByIdQuery);
        return result.isEmpty() ? Long.valueOf(0L) : result.get().getId();
    }

    @Override
    public List<Staff> getAllStaffByNursingHomeId(NursingHomeId nursingHomeId) {
        var getAllStaffMemberByNursingHomeIdQuery = new GetAllStaffMemberByNursingHomeIdQuery(nursingHomeId);
        var result = staffQueryServices.handle(getAllStaffMemberByNursingHomeIdQuery);
        return result.stream().toList();
    }

    @Override
    public List<Staff> getAllActiveStaffByNursingHomeId(NursingHomeId nursingHomeId) {
        var getAllStaffMemberByNursingHomeIdQuery = new GetAllStaffMemberByNursingHomeIdQuery(nursingHomeId);
        var result = staffQueryServices.handle(getAllStaffMemberByNursingHomeIdQuery);
        return result.stream()
                .filter(staff -> "ACTIVE".equals(staff.getStaffStatus().name()))
                .toList();

    }

    @Override
    public Optional<Staff> getStaffMemberWithNurseRoleAndActiveContract(Long staffId, NursingHomeId nursingHomeId) {
        var getStaffMemberWithNurseRoleAndActiveContractQuery = new GetStaffMemberWithNurseRoleAndActiveContractQuery(staffId, nursingHomeId);
        var result = staffQueryServices.handle(getStaffMemberWithNurseRoleAndActiveContractQuery);
        return result.stream()
                .filter(staff -> staff.getId().equals(staffId))
                .findFirst();

    }

    @Override
    public Long getStaffByUserId(Long userId) {
        var getStaffByUserIdQuery = new GetStaffByUserIdQuery(userId);
        var result = staffQueryServices.handle(getStaffByUserIdQuery);
        return result.isEmpty() ? Long.valueOf(0L) : result.get().getId();
    }

    @Override
    public Long fetchUserIdByStaffId(Long staffId) {
        var query = new GetStaffByIdQuery(staffId);
        var result = staffQueryServices.handle(query);
        return result.isEmpty() ? Long.valueOf(0L) : result.get().getUserId().getUserId();
    }
}