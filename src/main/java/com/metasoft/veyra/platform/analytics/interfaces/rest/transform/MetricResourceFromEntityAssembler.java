package com.metasoft.veyra.platform.analytics.interfaces.rest.transform;

import com.metasoft.veyra.platform.analytics.domain.model.aggregates.Metric;
import com.metasoft.veyra.platform.analytics.interfaces.rest.resources.MetricResource;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class MetricResourceFromEntityAssembler {

    public static MetricResource toResourceFromEntityList(List<Metric> metrics) {
        if (metrics.isEmpty()) {
            return new MetricResource(
                    Collections.emptyList(),
                    Collections.emptyList(),
                    "",
                    0L
            );
        }

        Map<Month, Long> monthlyAggregation = metrics.stream()
                .collect(Collectors.groupingBy(
                        metric -> metric.getEventDate().getMonth(),
                        Collectors.summingLong(Metric::getValue)
                ));

        List<Month> sortedMonths = monthlyAggregation.keySet().stream()
                .sorted()
                .toList();

        // Generate labels (month names in English)
        List<String> labels = sortedMonths.stream()
                .map(month -> month.getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                .toList();

        List<Long> values = sortedMonths.stream()
                .map(monthlyAggregation::get)
                .toList();

        long total = values.stream()
                .mapToLong(Long::longValue)
                .sum();

        String metricType = metrics.getFirst().getMetricType().name();

        return new MetricResource(labels, values, metricType, total);
    }
}