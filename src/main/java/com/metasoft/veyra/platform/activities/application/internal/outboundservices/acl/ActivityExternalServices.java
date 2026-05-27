package com.metasoft.veyra.platform.activities.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.nursing.interfaces.acl.NursingContextFacade;
import org.springframework.stereotype.Service;

@Service
public class ActivityExternalServices {

    private final NursingContextFacade nursingContextFacade;

    public ActivityExternalServices(NursingContextFacade nursingContextFacade) {
        this.nursingContextFacade = nursingContextFacade;
    }

    public boolean staffExists(Long staffId) {
        System.out.println(
                "ADVERTENCIA: Simulación temporal de staffExists. " +
                        "Siempre devolverá 'true'."
        );

        if (staffId == null || staffId <= 0) return false;
        return true;
    }

    public boolean residentExists(Long residentId) {
        if (residentId == null || residentId <= 0) return false;
        return true;
    }
}