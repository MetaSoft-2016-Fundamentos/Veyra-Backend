package com.metasoft.veyra.platform.analytics.domain.model.queries;

import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;

import java.time.Year;

public record GetStaffHiresByNursingHomeIdAndYearAndMonthQuery(
        NursingHomeId nursingHomeId,
        Integer year,
        Integer month) {

    private static final int MIN_YEAR = 1900;

    public GetStaffHiresByNursingHomeIdAndYearAndMonthQuery {
        int currentYear = Year.now().getValue();

        if (year == null) {
            throw new IllegalArgumentException("year must not be null");
        }
        if (year < MIN_YEAR || year > currentYear) {
            throw new IllegalArgumentException(
                    "year must be between %d and %d".formatted(MIN_YEAR, currentYear));
        }
        if (month == null) {
            throw new IllegalArgumentException("month must not be null");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("month must be between 1 and 12");
        }
    }
}
