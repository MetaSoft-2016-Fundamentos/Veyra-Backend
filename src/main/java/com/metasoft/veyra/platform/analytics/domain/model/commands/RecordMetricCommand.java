package com.metasoft.veyra.platform.analytics.domain.model.commands;

import com.metasoft.veyra.platform.analytics.domain.model.aggregates.Metric;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricType;

import java.time.LocalDate;

/**
 * Command object that carries data required to record a metric occurrence.
 *
 * <p>This object is used inside application/command services to request the
 * creation or aggregation of a {@link Metric}.
 * It represents a write-side intent and should be validated by the caller or
 * the application layer before handling.</p>
 *
 * Fields:
 * <ul>
 *   <li>nursingHomeId: identifier of the nursing home where the event occurred</li>
 *   <li>metricType: the type of metric to record</li>
 *   <li>metricCategory: category of the metric (optional domain classification)</li>
 *   <li>eventDate: the date used to bucket this metric (e.g., month/year)</li>
 * </ul>
 */
public record RecordMetricCommand(Long nursingHomeId,
                                  MetricType metricType,
                                  MetricCategory metricCategory,
                                  LocalDate eventDate) {
}
