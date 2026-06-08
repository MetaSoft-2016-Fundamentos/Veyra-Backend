package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateAdministratorCommand;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateAdministratorResource;

public class CreateAdministratorCommandFromResourceAssembler {
    public static CreateAdministratorCommand toCommandFromResource(CreateAdministratorResource resource) {
        return new CreateAdministratorCommand(resource.username(),resource.password());
    }
}
