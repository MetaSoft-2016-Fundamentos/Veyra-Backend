package com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Relative;
import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.UserId;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RelativeRepository extends JpaRepository<Relative,Long> {
    boolean existsByUserId(UserId userId);


    Optional<Relative> findByEmailAddress(EmailAddress emailAddress);
}
