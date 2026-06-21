package com.metasoft.veyra.platform.health.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.hcm.interfaces.acl.HcmContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("ExternalHcmServiceHealth")
public class ExternalHcmService {

    private final HcmContextFacade hcmContextFacade;

    public ExternalHcmService(HcmContextFacade hcmContextFacade) {
        this.hcmContextFacade = hcmContextFacade;
    }

    public Optional<Long> fetchUserIdByStaffId(Long staffId) {
        var userId = hcmContextFacade.fetchUserIdByStaffId(staffId);
        return userId == 0L ? Optional.empty() : Optional.of(userId);
    }
}
