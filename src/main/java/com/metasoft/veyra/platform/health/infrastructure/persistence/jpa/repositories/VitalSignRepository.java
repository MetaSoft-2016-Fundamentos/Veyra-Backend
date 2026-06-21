package com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSign;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VitalSignRepository extends JpaRepository<VitalSign, Long> {
  Page<VitalSign> findAllByResidentIdAndCreatedAtBetween(
    ResidentId residentId,
    LocalDateTime startDate,
    LocalDateTime endDate,
    Pageable pageable
  );

  List<VitalSign> findAllByResidentId(ResidentId residentId);
  Optional<VitalSign> findByResidentId(ResidentId residentId);
  boolean existsByResidentId(ResidentId residentId);
}
