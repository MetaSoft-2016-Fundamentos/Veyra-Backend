package com.metasoft.veyra.platform.tracking.domain.services;

import com.metasoft.veyra.platform.tracking.domain.model.commands.SeedMeasurementCommand;

public interface MeasurementCommandService {
    void handle(SeedMeasurementCommand command);

}
