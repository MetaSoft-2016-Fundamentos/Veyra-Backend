package com.metasoft.veyra.platform.nursing.domain.services;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Relative;
import com.metasoft.veyra.platform.nursing.domain.model.commands.AssignUserToRelativeCommand;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateRelativeCommand;
import com.metasoft.veyra.platform.nursing.domain.model.commands.UpdateRelativeCommand;

import java.util.Optional;

public interface RelativeCommandService {
    Long handle(CreateRelativeCommand command);
    Long handle (AssignUserToRelativeCommand command);
    Optional<Relative> handle (UpdateRelativeCommand command);
}
