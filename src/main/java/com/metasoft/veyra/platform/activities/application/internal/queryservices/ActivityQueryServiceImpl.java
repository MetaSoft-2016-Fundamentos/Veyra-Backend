package com.metasoft.veyra.platform.activities.application.internal.queryservices;

import com.metasoft.veyra.platform.activities.domain.model.aggregates.Activity;
import com.metasoft.veyra.platform.activities.domain.model.queries.GetActivitiesByNursingHomeIdQuery;
import com.metasoft.veyra.platform.activities.domain.model.queries.GetActivitiesByResidentIdQuery;
import com.metasoft.veyra.platform.activities.domain.model.queries.GetActivityByIdQuery;
import com.metasoft.veyra.platform.activities.domain.model.queries.GetAllActivitiesQuery;
import com.metasoft.veyra.platform.activities.domain.services.ActivityQueryService;
import com.metasoft.veyra.platform.activities.infrastructure.persistence.jpa.repositories.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link ActivityQueryService} interface.
 * <p>This class is responsible for handling queries related to the Activity aggregate.
 * It requires an {@link ActivityRepository}.</p>
 * @see ActivityQueryService
 * @see ActivityRepository
 */
@Service
public class ActivityQueryServiceImpl implements ActivityQueryService {

    private final ActivityRepository activityRepository;

    /**
     * Constructor of the class.
     * @param activityRepository the repository used for Activity queries
     */
    public ActivityQueryServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    // inherit javadoc
    @Override
    public List<Activity> handle(GetAllActivitiesQuery query) {
        return activityRepository.findAll();
    }

    // inherit javadoc
    @Override
    public List<Activity> handle(GetActivitiesByNursingHomeIdQuery query) {
        return activityRepository.findByNursingHomeId(query.nursingHomeId());
    }

    // inherit javadoc
    @Override
    public List<Activity> handle(GetActivitiesByResidentIdQuery query) {
        return activityRepository.findByResidentId(query.residentId());
    }

    // inherit javadoc
    @Override
    public Optional<Activity> handle(GetActivityByIdQuery query) {
        return activityRepository.findById(query.activityId());
    }
}
