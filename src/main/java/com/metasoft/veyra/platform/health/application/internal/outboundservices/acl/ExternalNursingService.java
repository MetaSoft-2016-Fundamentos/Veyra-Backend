package com.metasoft.veyra.platform.health.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import com.metasoft.veyra.platform.nursing.interfaces.acl.NursingContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("ExternalNursingServiceHealth")
public class ExternalNursingService {
    private final NursingContextFacade nursingContextFacade;

    public ExternalNursingService(NursingContextFacade nursingContextFacade) {
        this.nursingContextFacade = nursingContextFacade;
    }
    public Optional<ResidentId> fetchResidentById(Long residentId) {
        var query = nursingContextFacade.fetchResidentById(residentId);
        return query == 0L ? Optional.empty() : Optional.of(new ResidentId(query));
    }

    public Optional<Long> fetchStaffMemberIdByResidentId(Long residentId) {
        var staffMemberId = nursingContextFacade.fetchStaffMemberIdByResidentId(residentId);
        return staffMemberId == 0L ? Optional.empty() : Optional.of(staffMemberId);
    }
}
