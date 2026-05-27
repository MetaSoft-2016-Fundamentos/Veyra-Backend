package com.metasoft.veyra.platform.hcm.interfaces.rest.transform;

import com.metasoft.veyra.platform.hcm.domain.model.commands.UpdateContractStatusCommand;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.UpdateContractResource;

public class UpdateContractCommandFromResourceAssembler {
    public static UpdateContractStatusCommand toCommandFromResource(Long StaffMemberId, Long ContractId, UpdateContractResource resource){
        return new UpdateContractStatusCommand(StaffMemberId,ContractId,resource.newStatus());
    }

}

