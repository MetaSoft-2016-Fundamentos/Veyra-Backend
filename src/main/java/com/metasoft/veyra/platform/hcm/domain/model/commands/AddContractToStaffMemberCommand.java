package com.metasoft.veyra.platform.hcm.domain.model.commands;

import java.time.LocalDate;

public record AddContractToStaffMemberCommand(Long staffMemberId, LocalDate startDate, LocalDate endDate, String typeOfContract,
                                              String staffRole, String workShift) {
}
