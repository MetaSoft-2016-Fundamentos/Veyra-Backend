package com.metasoft.veyra.platform.analytics.application.internal.commandservices;
import com.metasoft.veyra.platform.analytics.application.internal.outboundservices.acl.ExternalNursingService;
import com.metasoft.veyra.platform.analytics.domain.model.aggregates.Metric;
import com.metasoft.veyra.platform.analytics.domain.model.commands.RecordMetricCommand;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.analytics.domain.services.MetricCommandService;
import com.metasoft.veyra.platform.analytics.infrastructure.persistence.jpa.repositories.MetricRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
@Service
@Transactional
public class MetricCommandServiceImpl implements MetricCommandService {
    private final MetricRepository metricRepository;
    private final ExternalNursingService externalNursingService;

    public MetricCommandServiceImpl(MetricRepository metricRepository,
                                    ExternalNursingService externalNursingService ) {
        this.metricRepository = metricRepository;
        this.externalNursingService = externalNursingService;
    }

    @Override
    public void handle(RecordMetricCommand command) {
        externalNursingService.fetchNursingHomeCreatedAt(command.nursingHomeId())
                .ifPresent(createdAt -> {
                    if (command.eventDate().getYear() < createdAt.getYear()) {
                        throw new IllegalArgumentException(
                                "Cannot record metrics before the nursing home's creation year (%d)"
                                        .formatted(createdAt.getYear()));
                    }
                });

        var nursingHomeId = new NursingHomeId(command.nursingHomeId());
        var existingMetric = metricRepository
                .findByNursingHomeIdAndMetricTypeAndEventDate(
                        nursingHomeId, command.metricType(), command.eventDate());

        if (existingMetric.isPresent()) {
            var metric = existingMetric.get();
            metric.incrementValue();
            metricRepository.save(metric);
        } else {
            var newMetric = new Metric(nursingHomeId, command.metricType(),
                    command.metricCategory(), command.eventDate());
            metricRepository.save(newMetric);
        }
    }
}