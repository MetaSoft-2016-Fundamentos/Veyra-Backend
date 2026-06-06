package com.metasoft.veyra.platform.analytics.domain.model.queries;

import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;

public record GetStaffTerminationsByNursingHomeIdAndYearAndMonthQuery(NursingHomeId nursingHomeId, Integer year,Integer month) {
public   GetStaffTerminationsByNursingHomeIdAndYearAndMonthQuery{
    if (year==null){
        throw new IllegalArgumentException("year must not be null");
    }
    if (year<1900||year>2025)
    {
        throw new IllegalArgumentException("year must be between 1900 and 2025");
    }
    if (month==null){
        throw new IllegalArgumentException("moth must not be null");
    }
    if (month<1||month>12)
    {
        throw new IllegalArgumentException("moth must be between 1 and 12");
    }
}
}
