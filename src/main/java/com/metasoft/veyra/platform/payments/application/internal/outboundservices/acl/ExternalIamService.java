package com.metasoft.veyra.platform.payments.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.iam.interfaces.acl.IamContextFacade;
import com.metasoft.veyra.platform.payments.domain.model.valueobjects.UserId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * External IAM Service - Anti-Corruption Layer
 * Translates IAM bounded context concepts to Payments bounded context
 */
@Service("ExternalIamPayments")
@Slf4j
public class ExternalIamService {

    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    /**
     * Fetches UserId by username from IAM context
     */
    public UserId fetchUserByUsername(String username) {
        log.debug("Fetching user by username: {}", username);
        Long userId = iamContextFacade.fetchUserIdByUsername(username);
        if (userId == null) {
            log.warn("User not found for username: {}", username);
            return null;
        }
        return new UserId(userId);
    }

    /**
     * Validates if a user exists in IAM system by userId
     * Used before creating subscriptions or processing payments
     */
    public boolean existsUserById(Long userId) {
        try {
            log.debug("Validating user existence for userId: {}", userId);
            boolean exists = iamContextFacade.existsUserById(userId);

            if (!exists) {
                log.warn("User {} not found in IAM system", userId);
            }

            return exists;
        } catch (Exception e) {
            log.error("Error validating user existence for userId {}: {}", userId, e.getMessage());
            return false;
        }
    }

    /**
     * Gets username for a given userId
     * Useful for logging or display purposes
     */
    public String getUsernameByUserId(Long userId) {
        try {
            log.debug("Fetching username for userId: {}", userId);
            return iamContextFacade.fetchUsernameByUserId(userId);
        } catch (Exception e) {
            log.error("Error fetching username for userId {}: {}", userId, e.getMessage());
            return null;
        }
    }
}