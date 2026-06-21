package com.metasoft.veyra.platform.nursing.interfaces.acl;

import java.time.LocalDate;
import java.util.Optional;

public interface NursingContextFacade {
    Long fetchNursingHomeById(Long id);
    boolean existsResidentByPersonProfile(Long personProfile);
    Long fetchResidentById(Long residentId);
    Long fetchAdministratorByUserId(Long userId);
    Optional<LocalDate> fetchNursingHomeCreatedAtById(Long id);

    Long fetchStaffMemberIdByResidentId(Long residentId);
}