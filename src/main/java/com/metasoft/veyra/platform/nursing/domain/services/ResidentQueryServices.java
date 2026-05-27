package com.metasoft.veyra.platform.nursing.domain.services;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Resident;
import com.metasoft.veyra.platform.nursing.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface ResidentQueryServices {
    List<Resident>handle(GetAllResidentsByNursingHomeIdQuery query);
    Optional<Resident>handle(GetResidentByIdQuery query);
    Optional<Resident>handle(GetResidentByPersonProfileQuery query);
    List<Resident>handle(GetActiveResidentsByNursingHomeId query);
    boolean handle(ExistsByResidentIdQuery query);
   List<Resident>handle(GetResidentsByRelativeIdQuery query);
}
