package com.metasoft.veyra.platform.analytics.domain.model.queries;

import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricType;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;

import java.time.Year;

public record GetMetricsByNursingHomeIdAndTypeAndYearQuery(NursingHomeId nursingHomeId,
                                                           MetricType metricType,
                                                           Integer year) {
    private static final int MIN_YEAR = 1900;

    public GetMetricsByNursingHomeIdAndTypeAndYearQuery {
        int currentYear = Year.now().getValue();

        if (nursingHomeId == null) {
            throw new IllegalArgumentException("nursingHomeId must not be null");
        }
        if (metricType == null) {
            throw new IllegalArgumentException("metricType must not be null");
        }
        if (year == null) {
            throw new IllegalArgumentException("year must not be null");
        }
        if (year < MIN_YEAR || year > currentYear) {
            throw new IllegalArgumentException(
                    "year must be between %d and %d".formatted(MIN_YEAR, currentYear));
        }
    }
}
