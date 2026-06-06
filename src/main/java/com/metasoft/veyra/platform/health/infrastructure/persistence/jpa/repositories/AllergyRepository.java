package com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.health.domain.model.aggregates.Allergy;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ResidentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AllergyRepository  extends JpaRepository<Allergy,Long> {
    Optional<Allergy> findByResidentId(ResidentId residentId);
    List<Allergy> findAllByResidentId(ResidentId residentId);
    boolean existsByResidentId(ResidentId residentId);

    boolean existsByResidentIdAndAllergenName(ResidentId residentId, String allergenName);

}
