package com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSignThreshold;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VitalSignThresholdRepository extends JpaRepository<VitalSignThreshold, Long> {
    Optional<VitalSignThreshold> findByResidentId(ResidentId residentId);
    boolean existsByResidentId(ResidentId residentId);
}
