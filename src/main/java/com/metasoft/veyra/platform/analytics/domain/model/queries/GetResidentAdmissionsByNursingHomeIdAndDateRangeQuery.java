package com.metasoft.veyra.platform.analytics.domain.model.queries;

import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;

import java.time.LocalDate;

public record GetResidentAdmissionsByNursingHomeIdAndDateRangeQuery(
        NursingHomeId nursingHomeId,
        LocalDate startDate,
        LocalDate endDate
) {
    public GetResidentAdmissionsByNursingHomeIdAndDateRangeQuery
    {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate and endDate must not be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be before or equal to endDate");
        }


    }
}