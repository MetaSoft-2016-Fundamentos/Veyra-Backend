package com.metasoft.veyra.platform.hcm.interfaces.rest.resources;

import java.time.LocalDate;

public record AddContractResource(LocalDate startDate, LocalDate endDate, String typeOfContract,
                                  String staffRole, String workShift ) {
}
