package com.metasoft.veyra.platform.analytics.domain.services;

import com.metasoft.veyra.platform.analytics.domain.model.commands.RecordMetricCommand;

public interface MetricCommandService {
    void handle(RecordMetricCommand command);
}
