package com.metasoft.veyra.platform.hcm.interfaces.rest.transform;

import com.metasoft.veyra.platform.hcm.domain.model.commands.AddContractToStaffMemberCommand;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.AddContractResource;

public class AddContractCommandFromResourceAssembler {
public static AddContractToStaffMemberCommand toCommandFromResource(Long id, AddContractResource resource){
    return new AddContractToStaffMemberCommand(id,resource.startDate(),resource.endDate(),resource.typeOfContract(),resource.staffRole(),
            resource.workShift());
}
}
