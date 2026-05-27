package com.metasoft.veyra.platform.profiles.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.profiles.domain.model.aggregates.PersonProfile;
import com.metasoft.veyra.platform.profiles.domain.model.valueobjects.Dni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonProfileRepository extends JpaRepository<PersonProfile,Long> {
    Optional<PersonProfile> findByDni(Dni dni);
    boolean existsByDni(Dni dni);
}
