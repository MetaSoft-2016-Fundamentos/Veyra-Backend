package com.metasoft.veyra.platform.analytics.domain.model.queries;

import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;

public record GetResidentAdmissionsByNursingHomeIdAndYearAndMonthQuery(NursingHomeId nursingHomeId,Integer year,Integer month)
{
public  GetResidentAdmissionsByNursingHomeIdAndYearAndMonthQuery{
    if (year == null ){
        throw new IllegalArgumentException("year cannot be null");
    }
    if (month == null ){
        throw new IllegalArgumentException("month cannot be null");
    }
    if ( year < 1900 || year > 2025) {
        throw new IllegalArgumentException("year must be between 1900 and 2100");
    }

    if ( month < 1 || month > 12) {
        throw new IllegalArgumentException("month must be between 1 and 12");
    }
}
}
