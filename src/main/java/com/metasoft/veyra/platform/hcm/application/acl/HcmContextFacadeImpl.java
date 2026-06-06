package com.metasoft.veyra.platform.hcm.application.acl;

import com.metasoft.veyra.platform.hcm.domain.model.queries.GetAllActiveStaffByContractWithNurseRoleByNursingHomeIdQuery;
import com.metasoft.veyra.platform.hcm.domain.model.queries.GetStaffByIdQuery;
import com.metasoft.veyra.platform.hcm.domain.model.queries.GetStaffMemberWithNurseRoleAndActiveContractQuery;
import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.hcm.domain.model.queries.GetAllStaffMemberByNursingHomeIdQuery;
import com.metasoft.veyra.platform.hcm.domain.services.StaffQueryServices;
import com.metasoft.veyra.platform.hcm.interfaces.acl.HcmContextFacade;
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
        var query = new GetAllActiveStaffByContractWithNurseRoleByNursingHomeIdQuery(nursingHomeId);
        return staffQueryServices.handle(query);
    }

    @Override
    public Optional<Staff> getStaffById(Long id) {
        var query = new GetStaffByIdQuery(id);
        return staffQueryServices.handle(query);
    }

    @Override
    public List<Staff> getAllStaffByNursingHomeId(NursingHomeId nursingHomeId) {
        var query = new GetAllStaffMemberByNursingHomeIdQuery(nursingHomeId);
        return staffQueryServices.handle(query);
    }

    @Override
    public List<Staff> getAllActiveStaffByNursingHomeId(NursingHomeId nursingHomeId) {
        var query = new GetAllStaffMemberByNursingHomeIdQuery(nursingHomeId);
        return staffQueryServices.handle(query).stream()
                .filter(staff -> staff.getStaffStatus() != null && "ACTIVE".equals(staff.getStaffStatus().name()))
                .toList();
    }

    @Override
    public Optional<Staff> getStaffMemberWithNurseRoleAndActiveContract(Long staffId, NursingHomeId nursingHomeId) {
        var query = new GetStaffMemberWithNurseRoleAndActiveContractQuery(staffId, nursingHomeId);
        return staffQueryServices.handle(query);
    }
}