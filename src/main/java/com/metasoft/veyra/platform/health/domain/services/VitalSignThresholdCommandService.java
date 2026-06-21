package com.metasoft.veyra.platform.health.domain.services;

import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSignThreshold;
import com.metasoft.veyra.platform.health.domain.model.commands.RegisterVitalSignThresholdCommand;

public interface VitalSignThresholdCommandService {
    VitalSignThreshold handle(RegisterVitalSignThresholdCommand command);
}
