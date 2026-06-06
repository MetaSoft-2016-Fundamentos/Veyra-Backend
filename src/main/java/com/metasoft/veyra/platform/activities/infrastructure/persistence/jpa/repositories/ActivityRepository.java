package com.metasoft.veyra.platform.activities.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.activities.domain.model.aggregates.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ActivityRepository
 * <p>This interface is used to interact with the database and perform CRUD and query operations on the Activity entity.</p>
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    /**
     * Finds all activities belonging to a specific nursing home.
     * @param nursingHomeId the ID of the nursing home
     * @return a list of activities for the given nursing home
     */
    List<Activity> findByNursingHomeId(Long nursingHomeId);

    /**
     * Finds all activities assigned to a specific resident.
     * @param residentId the ID of the resident
     * @return a list of activities for the given resident
     */
    List<Activity> findByResidentId(Long residentId);
}
