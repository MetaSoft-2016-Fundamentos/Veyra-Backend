package com.metasoft.veyra.platform.analytics.domain.model.queries;

import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;

import java.time.Year;

public record GetResidentAdmissionsByNursingHomeIdAndYearQuery(
        NursingHomeId nursingHomeId,
        Integer year) {

    private static final int MIN_YEAR = 1900;

    public GetResidentAdmissionsByNursingHomeIdAndYearQuery {
        int currentYear = Year.now().getValue();

        if (year == null) {
            throw new IllegalArgumentException("year cannot be null");
        }
        if (year < MIN_YEAR || year > currentYear) {
            throw new IllegalArgumentException(
                    "year must be between %d and %d".formatted(MIN_YEAR, currentYear));
        }
    }
}