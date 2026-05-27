package com.metasoft.veyra.platform.activities.application.internal.commandservices;

import com.metasoft.veyra.platform.activities.application.internal.outboundservices.acl.ActivityExternalServices;
import com.metasoft.veyra.platform.activities.domain.model.aggregates.Activity;
import com.metasoft.veyra.platform.activities.domain.model.commands.CompleteActivityCommand;
import com.metasoft.veyra.platform.activities.domain.model.commands.CreateActivityCommand;
import com.metasoft.veyra.platform.activities.domain.services.ActivityCommandService;
import com.metasoft.veyra.platform.activities.infrastructure.persistence.jpa.repositories.ActivityRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivityCommandServiceImpl implements ActivityCommandService {

    private final ActivityRepository activityRepository;
    private final ActivityExternalServices externalServices;

    public ActivityCommandServiceImpl(ActivityRepository activityRepository, ActivityExternalServices externalServices) {
        this.activityRepository = activityRepository;
        this.externalServices = externalServices;
    }

    @Override
    public Long handle(CreateActivityCommand command) {
        if (!externalServices.residentExists(command.residentId())) {
            throw new IllegalArgumentException("Resident with ID " + command.residentId() + " does not exist.");
        }
        if (!externalServices.staffExists(command.staffMemberId())) {
            throw new IllegalArgumentException("Staff member (Attendant) with ID " + command.staffMemberId() + " does not exist.");
        }

        var activity = new Activity(command);

        activityRepository.save(activity);
        return activity.getId();
    }

    @Override
    public void handle(CompleteActivityCommand command) {
        var activity = activityRepository.findById(command.activityId())
                .orElseThrow(() -> new IllegalArgumentException("Activity with ID " + command.activityId() + " not found."));
        activity.complete();
        activityRepository.save(activity);
    }
}