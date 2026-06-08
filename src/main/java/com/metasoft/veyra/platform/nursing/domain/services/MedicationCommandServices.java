package com.metasoft.veyra.platform.nursing.domain.services;

import com.metasoft.veyra.platform.nursing.domain.model.commands.CreateMedicationCommand;

public interface MedicationCommandServices {
    Long handle (CreateMedicationCommand command);
}
