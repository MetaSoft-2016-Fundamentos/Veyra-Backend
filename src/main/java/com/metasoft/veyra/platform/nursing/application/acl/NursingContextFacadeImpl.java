package com.metasoft.veyra.platform.nursing.application.acl;

import com.metasoft.veyra.platform.nursing.domain.model.queries.GetNursingHomeByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetResidentByIdQuery;
import com.metasoft.veyra.platform.nursing.domain.model.queries.GetResidentByPersonProfileQuery;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.PersonProfileId;
import com.metasoft.veyra.platform.nursing.domain.services.NursingHomeQueryServices;
import com.metasoft.veyra.platform.nursing.domain.services.ResidentQueryServices;
import com.metasoft.veyra.platform.nursing.interfaces.acl.NursingContextFacade;
import org.springframework.stereotype.Service;

@Service

public class NursingContextFacadeImpl implements NursingContextFacade {
    private final NursingHomeQueryServices nursingHomeQueryServices;
   private final ResidentQueryServices residentQueryServices;
    public NursingContextFacadeImpl(NursingHomeQueryServices nursingHomeQueryServices, ResidentQueryServices residentQueryServices) {
        this.nursingHomeQueryServices = nursingHomeQueryServices;
        this.residentQueryServices = residentQueryServices;
    }

    @Override
    public Long fetchNursingHomeById(Long id ) {
        var findNursingHomeById=new GetNursingHomeByIdQuery(id);
        var  nursingHome=nursingHomeQueryServices.handle(findNursingHomeById);
        return nursingHome.isEmpty()?Long.valueOf(0L):nursingHome.get().getId();
    }

    @Override
    public boolean existsResidentByPersonProfile(Long id) {
        var personProfile= new PersonProfileId(id);
        var query= new GetResidentByPersonProfileQuery(personProfile);
        var residentOpt= residentQueryServices.handle(query);
        return residentOpt.isPresent();
    }

    @Override
    public Long fetchResidentById(Long residentId) {
        var findResidentById= new GetResidentByIdQuery(residentId);
        var resident= residentQueryServices.handle(findResidentById);
        return resident.isEmpty()?Long.valueOf(0L):resident.get().getId();
    }
}
