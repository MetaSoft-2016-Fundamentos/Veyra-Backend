package com.metasoft.veyra.platform.nursing.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.hcm.interfaces.acl.HcmContextFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExternalHcmService {
    private final HcmContextFacade hcmContextFacade;

    public ExternalHcmService(HcmContextFacade hcmContextFacade) {
        this.hcmContextFacade = hcmContextFacade;
    }

    public List<Long> fetchAllActiveNursesIdsByNursingHomeId(Long nursingHomeId) {
        return hcmContextFacade.getAllActiveStaffWithNurseRoleByNursingHomeId(new NursingHomeId(nursingHomeId))
                .stream()
                .map(staff -> staff.getId())
                .toList();
    }

    public List<Staff> fetchAllActiveStaffIdsByNursingHomeId(NursingHomeId nursingHomeId) {
        return hcmContextFacade.getAllActiveStaffByNursingHomeId(nursingHomeId);
    }

    public Long getStaffById(Long id){
        return hcmContextFacade.getStaffById(id);
    }

    public Optional<Staff> getStaffMemberWithNurseRoleAndActiveContract(Long staffId, Long nursingHomeId) {
        return hcmContextFacade.getStaffMemberWithNurseRoleAndActiveContract(staffId, new NursingHomeId(nursingHomeId));
    }

    public Long getStaffByUserId(Long userId){
        return hcmContextFacade.getStaffByUserId(userId);
    }
}