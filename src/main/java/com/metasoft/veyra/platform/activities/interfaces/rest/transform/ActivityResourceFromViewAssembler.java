package com.metasoft.veyra.platform.activities.interfaces.rest.transform;

import com.metasoft.veyra.platform.activities.domain.model.valueobjects.ActivityView;
import com.metasoft.veyra.platform.activities.interfaces.rest.resources.ActivityResource;

import java.time.format.DateTimeFormatter;

public class ActivityResourceFromViewAssembler {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static ActivityResource toResourceFromView(ActivityView view) {
        String hourRange = String.format("%s-%s",
                view.startTime().format(TIME_FORMATTER),
                view.endTime().format(TIME_FORMATTER)
        );

        return new ActivityResource(
                view.activityId(),
                hourRange,
                view.attendantName(),
                view.activityName(),
                view.area(),
                view.status().toString()
        );
    }
}