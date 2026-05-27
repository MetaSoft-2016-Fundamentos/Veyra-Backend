package com.metasoft.veyra.platform.hcm.interfaces.rest.transform;

import com.metasoft.veyra.platform.hcm.domain.model.entities.Contract;
import com.metasoft.veyra.platform.hcm.interfaces.rest.resources.ContractResource;

public class ContractResourceFromEntityAssembler {
    public static ContractResource toResourceFromEntity(Contract entity)
    {
        return new ContractResource(entity.getId(),entity.getStaff().getId(),entity.getContractPeriod().startDate(), entity.getContractPeriod().endDate()
        , entity.getTypeOfContract().typeOfContract(),entity.getStaffRole().name(),entity.getWorkShift().name(),entity.getContractStatus().name());
    }
}
