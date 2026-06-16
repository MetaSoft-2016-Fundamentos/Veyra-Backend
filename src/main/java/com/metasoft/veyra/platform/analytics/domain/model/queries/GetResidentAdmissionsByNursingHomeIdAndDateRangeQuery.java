package com.metasoft.veyra.platform.analytics.domain.model.queries;

import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;

import java.time.LocalDate;

public record GetResidentAdmissionsByNursingHomeIdAndDateRangeQuery(
        NursingHomeId nursingHomeId,
        LocalDate startDate,
        LocalDate endDate
) {
    public GetResidentAdmissionsByNursingHomeIdAndDateRangeQuery {
        if (nursingHomeId == null) {
            throw new IllegalArgumentException("nursingHomeId must not be null");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate and endDate must not be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be before or equal to endDate");
        }
        if (endDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("endDate must not be in the future");
        }
    }
}