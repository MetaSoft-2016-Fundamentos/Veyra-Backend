package com.metasoft.veyra.platform.analytics.interfaces.rest.resources;

import java.util.List;

/**
 * HTTP Resource representation for analytics metrics.
 *
 * <p>This record is used as the JSON representation sent to API clients for
 * analytics endpoints. Prefer the term "Resource" to refer to HTTP payloads
 * (instead of "DTO").</p>
 *
 * Fields:
 * <ul>
 *   <li>labels: human-readable labels for chart buckets (e.g. month names)</li>
 *   <li>values: numeric values matching each label</li>
 *   <li>metricType: type of metric (stringified enum)</li>
 *   <li>total: computed total of the values</li>
 * </ul>
 */
public record MetricResource( List<String> labels,
                              List<Long> values,
                              String metricType,
                              Long total) {
}
