package com.metasoft.veyra.platform.analytics.application.internal.queryservices;

import com.metasoft.veyra.platform.analytics.domain.model.aggregates.Metric;
import com.metasoft.veyra.platform.analytics.domain.model.queries.*;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricType;
import com.metasoft.veyra.platform.analytics.domain.services.MetricQueryService;
import com.metasoft.veyra.platform.analytics.infrastructure.persistence.jpa.repositories.MetricRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricQueryServiceImpl implements MetricQueryService {
    private final MetricRepository metricRepository;
    public MetricQueryServiceImpl(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    @Override
    public List<Metric> handle(GetResidentActivesByNursingHomeIdAndYearQuery query) {
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYear(query.nursingHomeId(), MetricType.RESIDENT_ACTIVE,query.year());
    }

    @Override
    public List<Metric> handle(GetResidentAdmissionsByNursingHomeIdAndDateRangeQuery query) {
        return metricRepository.findByNursingHomeIdAndMetricTypeAndEventDateBetween(query.nursingHomeId(),MetricType.RESIDENT_ADMISSION,query.startDate(),query.endDate());
    }

    @Override
    public List<Metric> handle(GetResidentAdmissionsByNursingHomeIdAndYearAndMonthQuery query) {
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYearAndMonth(query.nursingHomeId(),MetricType.RESIDENT_ADMISSION,query.year(),query.month());
    }

    @Override
    public List<Metric> handle(GetResidentAdmissionsByNursingHomeIdAndYearQuery query) {
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYear(query.nursingHomeId(),MetricType.RESIDENT_ADMISSION,query.year());
    }

    @Override
    public List<Metric> handle(GetStaffTerminationsByNursingHomeIdAndYearQuery query) {
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYear(query.nursingHomeId(),MetricType.EMPLOYEE_TERMINATED,query.year());
    }

    @Override
    public List<Metric> handle(GetStaffTerminationsByNursingHomeIdAndYearAndMonthQuery query) {
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYearAndMonth(query.nursingHomeId(),MetricType.EMPLOYEE_TERMINATED,query.year(),query.month());
    }

    @Override
    public List<Metric> handle(GetStaffHiresByNursingHomeIdAndYearQuery query) {
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYear(query.nursingHomeId(),MetricType.EMPLOYEE_HIRED,query.year());
    }

    @Override
    public List<Metric> handle(GetStaffHiresByNursingHomeIdAndYearAndMonthQuery query) {
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYearAndMonth(query.nursingHomeId(), MetricType.EMPLOYEE_HIRED, query.year(), query.month());
    }
    @Override
    public List<Metric> handle(GetMetricsByNursingHomeIdAndTypeAndYearQuery query) {
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYear(
                query.nursingHomeId(),
                query.metricType(),
                query.year()
        );
    }
}
