package com.metasoft.veyra.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.profiles.domain.model.aggregates.BusinessProfile;
import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.BusinessName;
import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.Ruc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessProfileRepository extends JpaRepository<BusinessProfile,Long> {
    boolean existsByBusinessName(BusinessName businessName);

    boolean existsByRuc(Ruc ruc);

    Optional<BusinessProfile> findByRuc(Ruc ruc);
}
