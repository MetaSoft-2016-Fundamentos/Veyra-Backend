package com.metasoft.veyra.platform.analytics.domain.model.queries;

import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricType;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;

public record GetMetricsByNursingHomeIdAndTypeAndYearQuery(NursingHomeId nursingHomeId,
                                                           MetricType metricType,
                                                           Integer year) {
}
