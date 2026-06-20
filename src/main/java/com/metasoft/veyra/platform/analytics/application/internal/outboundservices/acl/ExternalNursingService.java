package com.metasoft.veyra.platform.analytics.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.nursing.interfaces.acl.NursingContextFacade;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service("ExternalAnalyticsNursing")
public class ExternalNursingService {
    private final NursingContextFacade nursingContextFacade;

    public ExternalNursingService(NursingContextFacade nursingContextFacade) {
        this.nursingContextFacade = nursingContextFacade;
    }

    public Optional<LocalDate> fetchNursingHomeCreatedAt(Long nursingHomeId) {
        return nursingContextFacade.fetchNursingHomeCreatedAtById(nursingHomeId);
    }

    public Long fetchNursingHomeById(Long nursingHomeId) {
        return nursingContextFacade.fetchNursingHomeById(nursingHomeId);
    }
}
