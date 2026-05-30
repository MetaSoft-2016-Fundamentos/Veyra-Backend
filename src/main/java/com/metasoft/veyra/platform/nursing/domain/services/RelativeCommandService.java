package com.metasoft.veyra.platform.nursing.domain.services;

import com.metasoft.veyra.platform.nursing.domain.model.commands.AssignUserToRelativeCommand;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateRelativeCommand;

public interface RelativeCommandService {
    Long handle(CreateRelativeCommand command);
    Long handle (AssignUserToRelativeCommand command);
}
