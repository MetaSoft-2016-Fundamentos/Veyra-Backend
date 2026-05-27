package com.metasoft.veyra.platform.activities.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.activities.domain.model.aggregates.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByActivityDateAndNursingHomeId_NursingHomeIdOrderByPeriod_StartTime(
            LocalDate date,
            Long nursingHomeId
    );
}