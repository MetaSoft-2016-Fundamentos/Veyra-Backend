package com.metasoft.veyra.platform.hcm.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.PersonProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByPersonProfileIdAndNursingHomeId(PersonProfileId personProfileId, NursingHomeId nursingHomeId);

    List<Staff> findByNursingHomeId(NursingHomeId nursingHomeId);
}
