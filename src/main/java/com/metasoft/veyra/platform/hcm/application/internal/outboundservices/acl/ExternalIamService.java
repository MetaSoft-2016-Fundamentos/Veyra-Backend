package com.metasoft.veyra.platform.hcm.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.UserId;
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

    public String createStaffAccount(String email) {
        return iamContextFacade.createRelativeAccount(email); // Reutilizamos la lógica de creación por email/token
    }

    public UserId fetchUserByUsername(String username) {
        Long userId = iamContextFacade.fetchUserIdByUsername(username);
        if (userId == null || userId==0L) {
            return null;
        }
        return new UserId(userId);
    }
}