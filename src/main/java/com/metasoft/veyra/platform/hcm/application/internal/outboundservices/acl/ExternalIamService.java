package com.metasoft.veyra.platform.hcm.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("externalIamServiceHcm")
public class ExternalIamService {
    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public Long createStaffUser(String dni, String password, String role) {
        return iamContextFacade.createUser(dni, password, List.of(role));
    }
}