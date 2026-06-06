package com.metasoft.veyra.platform.nursing.domain.services;

import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateAdministratorCommand;

public interface AdministratorCommandService {
    Long handle(CreateAdministratorCommand command);
}
