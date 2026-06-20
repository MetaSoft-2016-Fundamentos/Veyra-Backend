package com.metasoft.veyra.platform.hcm.domain.services;

import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.hcm.domain.model.commands.*;

import java.util.Optional;

public interface StaffCommandServices {
    Long handle(CreateStaffCommand command);
    Optional<Staff>handle(UpdateStaffCommand command);
    void handle(DeleteStaffCommand command);
    void handle(AddContractToStaffMemberCommand command);
    void handle(UpdateContractStatusCommand command);
    void handle(RegisterStaffUserCommand command);
}