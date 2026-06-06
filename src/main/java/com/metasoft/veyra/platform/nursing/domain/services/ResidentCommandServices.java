package com.metasoft.veyra.platform.nursing.domain.services;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Resident;
import com.metasoft.veyra.platform.nursing.domain.model.commands.*;

import java.util.Optional;

public interface ResidentCommandServices {
    Long handle (CreateResidentCommand command);
    Optional<Resident>handle(UpdateResidentCommand command);
    void handle (DeleteResidentCommand command);
    void handle(AssignedStaffMemberToResidentCommand command);
    void handle(ChangeOfRoomForTheResidentCommand command);
    void handle(AssignRoomForResidentCommand command);
}
