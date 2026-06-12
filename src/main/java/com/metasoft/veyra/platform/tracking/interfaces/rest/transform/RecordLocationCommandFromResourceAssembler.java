package com.metasoft.veyra.platform.tracking.interfaces.rest.transform;

import com.metasoft.veyra.platform.tracking.domain.model.commands.RecordLocationCommand;
import com.metasoft.veyra.platform.tracking.interfaces.rest.resources.RecordLocationResource;

public class RecordLocationCommandFromResourceAssembler {
    public static RecordLocationCommand toCommandFromResource(RecordLocationResource resource) {
        return new RecordLocationCommand(resource.deviceId(), resource.latitude(), resource.longitude());
    }
}
