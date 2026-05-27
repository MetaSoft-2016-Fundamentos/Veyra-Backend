package com.metasoft.veyra.platform.analytics.application.internal.eventhandlers;

import com.metasoft.veyra.platform.analytics.domain.model.commands.RecordMetricCommand;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricType;
import com.metasoft.veyra.platform.analytics.domain.services.MetricCommandService;
import com.metasoft.veyra.platform.nursing.domain.model.events.RetiredResidentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
@Service
public class RetiredResidentEventHandler {
    private final MetricCommandService metricCommandService;
   private static  final Logger LOGGER= LoggerFactory.getLogger(RetiredResidentEventHandler.class);
    public RetiredResidentEventHandler(MetricCommandService metricCommandService) {
        this.metricCommandService = metricCommandService;
    }
    @EventListener(RetiredResidentEvent.class)
    public void on(RetiredResidentEvent event){
        var nursingHomeId= event.getNursingHomeId();
        var residentId= event.getResidentId();
        LOGGER.info("Handling RetiredResidentEvent for Resident ID: {} in Nursing Home ID: {}", residentId, nursingHomeId);
        var command=new RecordMetricCommand(nursingHomeId, MetricType.RESIDENT_RETIRED, MetricCategory.RESIDENT, event.getOccurredOn());
        metricCommandService.handle(command);
    }

}
