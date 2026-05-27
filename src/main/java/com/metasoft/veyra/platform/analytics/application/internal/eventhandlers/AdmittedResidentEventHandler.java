package com.metasoft.veyra.platform.analytics.application.internal.eventhandlers;

import com.metasoft.veyra.platform.analytics.domain.model.commands.RecordMetricCommand;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricType;
import com.metasoft.veyra.platform.analytics.domain.services.MetricCommandService;
import com.metasoft.veyra.platform.nursing.domain.model.events.AdmittedResidentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class AdmittedResidentEventHandler {
    private final MetricCommandService metricCommandService;
    private static final Logger LOGGER= LoggerFactory.getLogger(AdmittedResidentEventHandler.class);


    public AdmittedResidentEventHandler(MetricCommandService metricCommandService) {
        this.metricCommandService = metricCommandService;
    }
    @EventListener(AdmittedResidentEvent.class)
    public void on(AdmittedResidentEvent event){
        var nursingHomeId=event.getNursingHomeId();
        var residentId=event.getResidentId();
        LOGGER.info("Handling AdmittedResidentEvent for nursingHomeId: {} and residentId: {}", nursingHomeId, residentId);
       var command= new RecordMetricCommand(nursingHomeId, MetricType.RESIDENT_ADMISSION, MetricCategory.RESIDENT, event.getOccurredOn());
       metricCommandService.handle(command);
    }

}
