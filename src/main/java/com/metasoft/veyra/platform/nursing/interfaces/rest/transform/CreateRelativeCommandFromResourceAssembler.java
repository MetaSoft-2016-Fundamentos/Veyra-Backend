package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;
import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateRelativeCommand;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateRelativeResource;
public class CreateRelativeCommandFromResourceAssembler {
    public static CreateRelativeCommand toCommandFromResource(CreateRelativeResource resource, Long nursingHomeId) {
        return new CreateRelativeCommand(resource.firstName(),resource.lastName(),resource.email(),resource.residentId(),nursingHomeId);
    }
}