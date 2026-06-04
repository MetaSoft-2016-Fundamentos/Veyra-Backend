package com.metasoft.veyra.platform.health.domain.services;

import com.metasoft.veyra.platform.health.domain.model.commands.RegisterMedicalConditionCommand;

public interface MedicalConditionCommandService {
  Long handle(RegisterMedicalConditionCommand command);
}
