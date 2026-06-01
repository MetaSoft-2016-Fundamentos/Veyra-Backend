package com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication,Long> {
    boolean existsByResidentIdAndName(Long residentId, String name);

    boolean existsByResidentId(Long residentId);
    List<Medication> findByResidentId(Long residentId);
}
