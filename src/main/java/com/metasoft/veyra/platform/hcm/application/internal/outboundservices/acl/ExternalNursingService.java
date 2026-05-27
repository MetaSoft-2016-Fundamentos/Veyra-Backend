package com.metasoft.veyra.platform.hcm.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.nursing.interfaces.acl.NursingContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ExternalNursingService {
    private final NursingContextFacade nursingContextFacade;

    public ExternalNursingService(NursingContextFacade nursingContextFacade) {
        this.nursingContextFacade = nursingContextFacade;
    }
    public boolean existsResidentByPersonProfile(Long id)
    {
        return this.nursingContextFacade.existsResidentByPersonProfile(id);
    }
public Optional<NursingHomeId>fetchNursingHomeById(Long id){
var nursingHomeId=nursingContextFacade.fetchNursingHomeById(id);
return nursingHomeId==0L?Optional.empty():Optional.of(new NursingHomeId(id));
    }
}
