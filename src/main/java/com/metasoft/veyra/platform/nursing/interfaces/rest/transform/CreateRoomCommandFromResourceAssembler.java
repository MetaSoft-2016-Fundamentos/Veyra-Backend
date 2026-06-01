package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateARoomToTheNursingHomeCommand;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.CreateRoomResource;

public class CreateRoomCommandFromResourceAssembler {
    public static CreateARoomToTheNursingHomeCommand toCommandFromResource(Long nursingHomeId,CreateRoomResource resource)
    {
        return new CreateARoomToTheNursingHomeCommand( nursingHomeId,resource.capacity(),resource.type(),resource.roomNumber());
    }
}
