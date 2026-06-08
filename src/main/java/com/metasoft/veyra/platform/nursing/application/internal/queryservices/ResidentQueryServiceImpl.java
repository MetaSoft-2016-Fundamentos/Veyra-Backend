package com.metasoft.veyra.platform.nursing.application.internal.queryservices;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Resident;
import com.metasoft.veyra.platform.nursing.domain.model.queries.*;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.ResidentState;
import com.metasoft.veyra.platform.nursing.domain.services.ResidentQueryServices;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.RelativeRepository;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.ResidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResidentQueryServiceImpl implements ResidentQueryServices {
    private final ResidentRepository residentRepository;
   private final RelativeRepository relativeRepository;
    public ResidentQueryServiceImpl(ResidentRepository residentRepository, RelativeRepository relativeRepository) {
        this.residentRepository = residentRepository;
        this.relativeRepository = relativeRepository;
    }

    @Override
    public List<Resident> handle(GetAllResidentsByNursingHomeIdQuery query) {
        return residentRepository.findAllByNursingHomeId(query.nursingHomeId());
    }

    @Override
    public Optional<Resident> handle(GetResidentByIdQuery query) {
        return residentRepository.findById(query.id());
    }

    @Override
    public Optional<Resident> handle(GetResidentByPersonProfileQuery query) {
        return residentRepository.findByPersonProfileId(query.id());
    }

    @Override
    public List<Resident> handle(GetActiveResidentsByNursingHomeId query) {
        return residentRepository.findByNursingHomeIdAndResidentStatus(query.nursingHomeId(), ResidentState.ACTIVE);
    }


    @Override
    public boolean handle(ExistsByResidentIdQuery query) {
        return residentRepository.existsById(query.residentId());

    }

    @Override
    public Optional<Resident> handle(GetResidentByRelativeIdQuery query) {
        var relative = relativeRepository.findById(query.relativeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Relative with ID " + query.relativeId() + " not found."));

        if (relative.getResident() == null || relative.getResident().getId() == null) {
            return Optional.empty();
        }

        return residentRepository.findById(relative.getResident().getId());
    }

}
