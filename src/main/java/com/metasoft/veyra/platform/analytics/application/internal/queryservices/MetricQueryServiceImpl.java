package com.metasoft.veyra.platform.analytics.application.internal.queryservices;

import com.metasoft.veyra.platform.analytics.application.internal.outboundservices.acl.ExternalNursingService;
import com.metasoft.veyra.platform.analytics.domain.model.aggregates.Metric;
import com.metasoft.veyra.platform.analytics.domain.model.queries.*;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricType;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.analytics.domain.services.MetricQueryService;
import com.metasoft.veyra.platform.analytics.infrastructure.persistence.jpa.repositories.MetricRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricQueryServiceImpl implements MetricQueryService {
    private final MetricRepository metricRepository;
    private final ExternalNursingService externalNursingService;

    public MetricQueryServiceImpl(MetricRepository metricRepository,
                                  ExternalNursingService externalNursingService) {
        this.metricRepository = metricRepository;
        this.externalNursingService = externalNursingService;
    }

    private void validateNursingHomeExists(NursingHomeId nursingHomeId) {
        Long id = externalNursingService.fetchNursingHomeById(nursingHomeId.nursingHomeId());
        if (id == 0L) {
            throw new IllegalArgumentException(
                    "Nursing home with id %d not found".formatted(nursingHomeId.nursingHomeId()));
        }
    }

    @Override
    public List<Metric> handle(GetResidentAdmissionsByNursingHomeIdAndYearQuery query) {
        validateNursingHomeExists(query.nursingHomeId());
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYear(
                query.nursingHomeId(), MetricType.RESIDENT_ADMISSION, query.year());
    }

    @Override
    public List<Metric> handle(GetResidentAdmissionsByNursingHomeIdAndDateRangeQuery query) {
        validateNursingHomeExists(query.nursingHomeId());
        return metricRepository.findByNursingHomeIdAndMetricTypeAndEventDateBetween(
                query.nursingHomeId(), MetricType.RESIDENT_ADMISSION, query.startDate(), query.endDate());
    }

    @Override
    public List<Metric> handle(GetResidentAdmissionsByNursingHomeIdAndYearAndMonthQuery query) {
        validateNursingHomeExists(query.nursingHomeId());
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYearAndMonth(
                query.nursingHomeId(), MetricType.RESIDENT_ADMISSION, query.year(), query.month());
    }

    @Override
    public List<Metric> handle(GetResidentActivesByNursingHomeIdAndYearQuery query) {
        validateNursingHomeExists(query.nursingHomeId());
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYear(
                query.nursingHomeId(), MetricType.RESIDENT_ACTIVE, query.year());
    }

    @Override
    public List<Metric> handle(GetStaffHiresByNursingHomeIdAndYearQuery query) {
        validateNursingHomeExists(query.nursingHomeId());
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYear(
                query.nursingHomeId(), MetricType.EMPLOYEE_HIRED, query.year());
    }

    @Override
    public List<Metric> handle(GetStaffHiresByNursingHomeIdAndYearAndMonthQuery query) {
        validateNursingHomeExists(query.nursingHomeId());
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYearAndMonth(
                query.nursingHomeId(), MetricType.EMPLOYEE_HIRED, query.year(), query.month());
    }

    @Override
    public List<Metric> handle(GetStaffTerminationsByNursingHomeIdAndYearQuery query) {
        validateNursingHomeExists(query.nursingHomeId());
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYear(
                query.nursingHomeId(), MetricType.EMPLOYEE_TERMINATED, query.year());
    }

    @Override
    public List<Metric> handle(GetStaffTerminationsByNursingHomeIdAndYearAndMonthQuery query) {
        validateNursingHomeExists(query.nursingHomeId());
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYearAndMonth(
                query.nursingHomeId(), MetricType.EMPLOYEE_TERMINATED, query.year(), query.month());
    }

    @Override
    public List<Metric> handle(GetMetricsByNursingHomeIdAndTypeAndYearQuery query) {
        validateNursingHomeExists(query.nursingHomeId());
        return metricRepository.findByNursingHomeIdAndMetricTypeAndYear(
                query.nursingHomeId(), query.metricType(), query.year());
    }
}
