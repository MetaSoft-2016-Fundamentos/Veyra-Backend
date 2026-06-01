package com.metasoft.veyra.platform.nursing.application.internal.eventhandlers;

import com.metasoft.veyra.platform.nursing.domain.services.NursingHomeCommandServices;
import org.springframework.stereotype.Service;

@Service
public class UserActivatedEventHandler {
    private final NursingHomeCommandServices nursingHomeCommandServices;

    public UserActivatedEventHandler(NursingHomeCommandServices nursingHomeCommandServices) {
        this.nursingHomeCommandServices = nursingHomeCommandServices;
    }

}
