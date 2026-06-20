package com.metasoft.veyra.platform.nursing.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.iam.interfaces.acl.IamContextFacade;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.UserId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("externalIamServiceNursing")
public class ExternalIamService {

    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Creates a user in IAM with specific roles
     * @throws RuntimeException if username already exists or role not found
     */
    public UserId createUser(String username, String password, List<String> roleNames) {
        Long userId = iamContextFacade.createUser(username, password, roleNames);
        return new UserId(userId);
    }

    /**
     * Fetches user by username
     * @return UserId if found, null if not found
     */
    public UserId fetchUserByUsername(String username) {
        Long userId = iamContextFacade.fetchUserIdByUsername(username);
        if (userId == null || userId==0L) {
            return null;
        }
        return new UserId(userId);
    }

    /**
     * Verifies if a user exists
     */
    public boolean userExists(String username) {
        return fetchUserByUsername(username) != null;
    }
    /**
     * Creates a relative account in IAM with a temporary password.
     * @return activation token to be sent to the relative's email
     */
    public String createRelativeAccount(String email) {
        return iamContextFacade.createRelativeAccount(email);
    }
}