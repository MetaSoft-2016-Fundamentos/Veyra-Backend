package com.metasoft.veyra.platform.analytics.domain.services;

import com.metasoft.veyra.platform.analytics.domain.model.aggregates.Metric;
import com.metasoft.veyra.platform.analytics.domain.model.queries.*;

import java.util.List;

/**
 * Domain query service that exposes read-only operations to obtain analytics
 * metrics. Implementations provide the query handling logic and return domain
 * aggregates that controllers or assemblers can transform into Resources.
 */
public interface MetricQueryService {
    List<Metric>handle(GetResidentActivesByNursingHomeIdAndYearQuery query);
    List<Metric>handle(GetResidentAdmissionsByNursingHomeIdAndDateRangeQuery query);
    List<Metric>handle(GetResidentAdmissionsByNursingHomeIdAndYearAndMonthQuery query);
    List<Metric>handle(GetResidentAdmissionsByNursingHomeIdAndYearQuery query);
    List<Metric>handle(GetStaffTerminationsByNursingHomeIdAndYearQuery query);
    List<Metric>handle(GetStaffTerminationsByNursingHomeIdAndYearAndMonthQuery query);
    List<Metric>handle(GetStaffHiresByNursingHomeIdAndYearQuery query);
    List<Metric>handle(GetStaffHiresByNursingHomeIdAndYearAndMonthQuery query);
    List<Metric> handle(GetMetricsByNursingHomeIdAndTypeAndYearQuery query);

}
