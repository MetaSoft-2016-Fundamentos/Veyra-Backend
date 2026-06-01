package com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.NursingHome;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.BusinessProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NursingHomeRepository extends JpaRepository<NursingHome,Long> {
    Optional<NursingHome> findByBusinessProfileId(BusinessProfileId businessProfileId);
    Optional<NursingHome> findByAdministratorId(Long administratorId);
    boolean existsByAdministratorId(Long administratorId);
}
