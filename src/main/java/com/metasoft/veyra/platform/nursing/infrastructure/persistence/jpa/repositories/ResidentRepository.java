package com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Resident;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.PersonProfileId;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.ResidentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResidentRepository extends JpaRepository<Resident,Long> {
    Optional<Resident>findByPersonProfileId( PersonProfileId personProfileId);
    List<Resident>findAllByNursingHomeId(Long nursingHomeId);
    List<Resident> findByNursingHomeIdAndResidentStatus(Long nursingHomeId, ResidentState residentState);
    boolean existsByNursingHomeIdAndPersonProfileId(Long nursingHomeId, PersonProfileId personProfileId);
    boolean existsByIdAndNursingHomeId(Long id, Long nursingHomeId);
}

