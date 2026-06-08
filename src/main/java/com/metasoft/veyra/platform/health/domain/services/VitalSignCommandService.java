package com.metasoft.veyra.platform.health.domain.services;

import com.metasoft.veyra.platform.health.domain.model.commands.ValidateVitalSignCommand;

public interface VitalSignCommandService {
    void handle(ValidateVitalSignCommand command);
}
