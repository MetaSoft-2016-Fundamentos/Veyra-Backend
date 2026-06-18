package com.metasoft.veyra.platform.tracking.domain.services;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.GpsLocation;
import com.metasoft.veyra.platform.tracking.domain.model.commands.RecordLocationCommand;

public interface LocationCommandService {
    GpsLocation handle(RecordLocationCommand command);
}
