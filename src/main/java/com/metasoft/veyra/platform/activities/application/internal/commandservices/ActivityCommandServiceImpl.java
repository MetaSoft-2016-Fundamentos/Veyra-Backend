package com.metasoft.veyra.platform.activities.application.internal.commandservices;

import com.metasoft.veyra.platform.activities.domain.model.aggregates.Activity;
import com.metasoft.veyra.platform.activities.domain.model.commands.CompleteActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.commands.CreateActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.commands.DeleteActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.commands.UpdateActivityCommand;
import com.metasoft.veyra.platform.activities.application.internal.outboundservices.acl.ExternalNursingService;
import com.metasoft.veyra.platform.activities.domain.services.ActivityCommandService;
import com.metasoft.veyra.platform.activities.infrastructure.persistence.jpa.repositories.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the {@link ActivityCommandService} interface.
 * <p>This class is responsible for handling commands related to the Activity aggregate.
 * It requires an {@link ActivityRepository}.</p>
 * @see ActivityCommandService
 * @see ActivityRepository
 */
@Service
public class ActivityCommandServiceImpl implements ActivityCommandService {

    private final ActivityRepository activityRepository;
    private final ExternalNursingService externalNursingService;

    /**
     * Constructor of the class.
     * @param activityRepository the repository used for Activity persistence
     * @param externalNursingService the ACL service for nursing context validation
     */
    public ActivityCommandServiceImpl(ActivityRepository activityRepository, ExternalNursingService externalNursingService) {
        this.activityRepository = activityRepository;
        this.externalNursingService = externalNursingService;
    }

    // inherit javadoc
    @Override
    public Long handle(CreateActivityCommand command) {
        if (!externalNursingService.existsNursingHomeById(command.nursingHomeId()))
            throw new IllegalArgumentException("Nursing home with id %s not found".formatted(command.nursingHomeId()));
        if (!externalNursingService.existsResidentById(command.residentId()))
            throw new IllegalArgumentException("Resident with id %s not found".formatted(command.residentId()));
        var activity = new Activity(command);
        try {
            activityRepository.save(activity);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving activity: %s".formatted(e.getMessage()));
        }
        return activity.getId();
    }

    // inherit javadoc
    @Override
    public Optional<Activity> handle(UpdateActivityCommand command) {
        var result = activityRepository.findById(command.activityId());
        if (result.isEmpty())
            throw new IllegalArgumentException("Activity with id %s not found".formatted(command.activityId()));
        var activityToUpdate = result.get();
        try {
            var updated = activityRepository.save(activityToUpdate.update(command));
            return Optional.of(updated);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating activity: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    public void handle(DeleteActivityCommand command) {
        if (!activityRepository.existsById(command.activityId()))
            throw new IllegalArgumentException("Activity with id %s not found".formatted(command.activityId()));
        try {
            activityRepository.deleteById(command.activityId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting activity: %s".formatted(e.getMessage()));
        }
    }

    // inherit javadoc
    @Override
    public void handle(CompleteActivityCommand command) {
        var activity = activityRepository.findById(command.activityId())
                .orElseThrow(() -> new IllegalArgumentException("Activity with id %s not found".formatted(command.activityId())));
        activity.advance();
        try {
            activityRepository.save(activity);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while advancing activity: %s".formatted(e.getMessage()));
        }
    }
}
