package com.metasoft.veyra.platform.communication.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.communication.domain.exceptions.UserNotFoundForPushException;
import com.metasoft.veyra.platform.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

@Service("ExternalIamCommunication")
public class ExternalIamService {

    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public void ensureUserExists(Long userId) {
        if (!iamContextFacade.existsUserById(userId)) {
            throw new UserNotFoundForPushException(userId);
        }
    }
}
