package com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Administrator;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator,Long> {
    boolean existsByUserId(UserId userId);

    Optional<Administrator> findByUserId(UserId userId);

}
