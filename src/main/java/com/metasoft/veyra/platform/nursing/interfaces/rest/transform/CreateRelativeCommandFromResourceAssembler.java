package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateRelativeCommand;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateRelativeResource;
public class CreateRelativeCommandFromResourceAssembler {
    public static CreateRelativeCommand toCommandFromResource(CreateRelativeResource resource) {
        return new CreateRelativeCommand(resource.email(),resource.firstName(),resource.lastName());
    }
}