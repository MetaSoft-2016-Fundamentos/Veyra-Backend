package com.metasoft.veyra.platform.hcm.interfaces.rest.resources;

import java.time.LocalDate;

public record ContractResource(Long id,Long staffMemberId,LocalDate startDate, LocalDate endDate, String typeOfContract,
                               String staffRole, String workShift, String status ) {
}
