package com.metasoft.veyra.platform.iam.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.iam.domain.model.valueobjects.EntityId;
import com.metasoft.veyra.platform.nursing.interfaces.acl.NursingContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("ExternalNursingServiceIam")
public class ExternalNursingService {
  private final NursingContextFacade nursingContextFacade;
  public ExternalNursingService(NursingContextFacade nursingContextFacade) {
    this.nursingContextFacade = nursingContextFacade;
  }
  public Optional<EntityId> fetchAdministratorEntityId(Long userId){
    var query = nursingContextFacade.fetchAdministratorByUserId(userId);
    return query==0L?Optional.empty():Optional.of(new EntityId(query));
  }


}