package com.metasoft.veyra.platform.activities.application.internal.queryservices;

import com.metasoft.veyra.platform.activities.domain.model.aggregates.Activity;
import com.metasoft.veyra.platform.activities.domain.model.queries.GetActivitiesByDateAndNursingHomeQuery;
import com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityView;
import com.metasoft.veyra.platform.activities.domain.services.ActivityQueryService;
import com.metasoft.veyra.platform.activities.infrastructure.persistence.jpa.repositories.ActivityRepository;
import com.metasoft.veyra.platform.hcm.domain.model.aggregates.Staff;
import com.metasoft.veyra.platform.nursing.domain.model.aggregates.Resident;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityQueryServiceImpl implements ActivityQueryService {

    private final ActivityRepository activityRepository;

    public ActivityQueryServiceImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public List<ActivityView> handle(GetActivitiesByDateAndNursingHomeQuery query) {

        List<Activity> activities = activityRepository.findByActivityDateAndNursingHomeId_NursingHomeIdOrderByPeriod_StartTime(
                query.date(),
                query.nursingHomeId()
        );

        return activities.stream()
                .map(this::mapActivityToActivityView)
                .collect(Collectors.toList());
    }

    private ActivityView mapActivityToActivityView(Activity activity) {

        Resident resident = activity.getResident();
        Staff staff = activity.getStaff();
        String residentName = "Residente " + resident.getId();
        String attendantName = "Staff " + staff.getId();

        return new ActivityView(
                activity.getId(),
                activity.getName(),
                activity.getPeriod().getStartTime(),
                activity.getPeriod().getEndTime(),
                activity.getArea().getAreaCode(),
                activity.getStatus(),
                activity.getResidentId(),
                residentName,
                activity.getStaffMemberId(),
                attendantName
        );
    }
}