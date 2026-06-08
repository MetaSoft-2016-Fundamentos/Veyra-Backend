package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.commands.UpdateRelativeCommand;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.UpdateRelativeResource;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailAddress;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.PersonName;

public class UpdateRelativeCommandFromResourceAssembler {
    public static UpdateRelativeCommand toCommandFromResource(Long relativeId, UpdateRelativeResource resource){
        return new UpdateRelativeCommand(relativeId, new PersonName(resource.firstName(),resource.lastName()),new EmailAddress(resource.email()),resource.residentId());
    }

}
