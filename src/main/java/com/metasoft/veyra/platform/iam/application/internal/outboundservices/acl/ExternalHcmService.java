package com.metasoft.veyra.platform.iam.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.hcm.interfaces.acl.HcmContextFacade;
import com.metasoft.veyra.platform.iam.domain.model.valueobjects.EntityId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("ExternalHcmServiceIam")
public class ExternalHcmService {
    private final HcmContextFacade hcmContextFacade;

    public ExternalHcmService(HcmContextFacade hcmContextFacade){
        this.hcmContextFacade= hcmContextFacade;
    }

    public Optional<EntityId> fetchStaffEntityId(Long userId){
        var query = hcmContextFacade.getStaffByUserId(userId);
        return query==0L?Optional.empty():Optional.of(new EntityId(query));
    }
}
