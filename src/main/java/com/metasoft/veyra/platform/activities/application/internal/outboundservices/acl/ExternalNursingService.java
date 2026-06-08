package com.metasoft.veyra.platform.activities.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.nursing.interfaces.acl.NursingContextFacade;
import org.springframework.stereotype.Service;

/**
 * ACL service that wraps the NursingContextFacade for use within the activities bounded context.
 */
@Service("activitiesExternalNursingService")
public class ExternalNursingService {

    private final NursingContextFacade nursingContextFacade;

    public ExternalNursingService(NursingContextFacade nursingContextFacade) {
        this.nursingContextFacade = nursingContextFacade;
    }

    /**
     * Checks whether a nursing home with the given ID exists.
     * @param nursingHomeId the ID to check
     * @return true if the nursing home exists, false otherwise
     */
    public boolean existsNursingHomeById(Long nursingHomeId) {
        return nursingContextFacade.fetchNursingHomeById(nursingHomeId) != 0L;
    }

    /**
     * Checks whether a resident with the given ID exists.
     * @param residentId the ID to check
     * @return true if the resident exists, false otherwise
     */
    public boolean existsResidentById(Long residentId) {
        return nursingContextFacade.fetchResidentById(residentId) != 0L;
    }
}
