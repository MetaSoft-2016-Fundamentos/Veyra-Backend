package com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSign;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VitalSignRepository extends JpaRepository<VitalSign,Long> {
    List<VitalSign> findAllByResidentId(ResidentId residentId);
    Optional<VitalSign> findByResidentId(ResidentId residentId);
    boolean existsByResidentId(ResidentId residentId);
}
