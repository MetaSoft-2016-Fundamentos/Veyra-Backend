package com.metasoft.veyra.platform.health.domain.services;

import com.metasoft.veyra.platform.health.domain.model.commands.RegisterAllergyCommand;

public interface AllergyCommandService {
    Long handle(RegisterAllergyCommand command);
}
