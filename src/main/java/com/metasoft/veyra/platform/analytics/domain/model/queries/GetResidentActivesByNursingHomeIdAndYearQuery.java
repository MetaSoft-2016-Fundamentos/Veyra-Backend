package com.metasoft.veyra.platform.analytics.domain.model.queries;

import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;

public record GetResidentActivesByNursingHomeIdAndYearQuery(NursingHomeId nursingHomeId , Integer year) {
    public GetResidentActivesByNursingHomeIdAndYearQuery{
        if (year==null){
            throw new IllegalArgumentException("year cannot be null");
        }
        if ( year < 1900 || year > 2025) {
            throw new IllegalArgumentException("year must be between 1900 and 2025");
        }

    }

}
