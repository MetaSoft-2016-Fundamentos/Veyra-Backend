package com.metasoft.veyra.platform.analytics.domain.model.aggregates;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricType;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Domain aggregate that represents an analytics metric for a nursing home.
 *
 * <p>Each Metric records the occurrence count (value) of a specific
 * {@link MetricType} within a given {@link #eventDate} (used for bucketing by
 * month/year) for a {@link NursingHomeId}.</p>
 *
 * <p>Invariants and behavior:
 * <ul>
 *   <li>When a metric is first created, its <code>value</code> is initialized to 1.</li>
 *   <li>Call {@link #incrementValue()} to increment the aggregated count.</li>
 * </ul>
 * </p>
 *
 * <p>Persistence: this is a JPA entity and an aggregate root; use repository
 * interfaces to query and persist instances.</p>
 */
@Entity
@Getter
public class Metric extends AuditableAbstractAggregateRoot<Metric> {
    @Embedded
    private NursingHomeId nursingHomeId;
    @Enumerated(EnumType.STRING)
    private MetricType metricType;
    @Enumerated(EnumType.STRING)
    private MetricCategory metricCategory;
    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false)
    private Long value;

    public Metric() {}
    public Metric(NursingHomeId nursingHomeId, MetricType metricType,
                  MetricCategory metricCategory,LocalDate eventDate) {
        this.nursingHomeId = nursingHomeId;
        this.metricType = metricType;
        this.metricCategory = metricCategory;
        this.value = 1L;
        this.eventDate=eventDate;
    }
    /**
     * Increment the recorded metric value by one. This models an idempotent
     * aggregation operation when the caller intends to tally occurrences.
     */
    public void incrementValue() {
        if (this.value == Long.MAX_VALUE) {
            throw new ArithmeticException("Metric value has reached its maximum limit");
        }
        this.value++;
    }}
