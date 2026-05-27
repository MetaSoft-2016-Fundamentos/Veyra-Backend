package com.metasoft.veyra.platform.analytics.application.internal.eventhandlers;

import com.metasoft.veyra.platform.analytics.domain.model.commands.RecordMetricCommand;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricType;
import com.metasoft.veyra.platform.analytics.domain.services.MetricCommandService;
import com.metasoft.veyra.platform.hcm.domain.model.events.EmployeeHiredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class HiredEmployeeEventHandler {
    private final MetricCommandService metricCommandService;
    private static final Logger LOGGER = LoggerFactory.getLogger(HiredEmployeeEventHandler.class);
    public HiredEmployeeEventHandler(MetricCommandService metricCommandService) {
        this.metricCommandService = metricCommandService;
    }
    @EventListener(EmployeeHiredEvent.class)
    public void on(EmployeeHiredEvent event)
    {
          var nursingHomeId= event.getNursingHomeId().nursingHomeId();
          var employeeId=event.getEmployeeId();
          var contractId= event.getContractId();
          LOGGER.info("Handling EmployeeHiredEvent for Employee ID: {} in Nursing Home ID: {} with Contract ID: {}", employeeId, nursingHomeId, contractId);
         var recordMetricCommand= new RecordMetricCommand(event.getNursingHomeId().nursingHomeId(), MetricType.EMPLOYEE_HIRED, MetricCategory.STAFF,event.getOccurredOn());
         metricCommandService.handle(recordMetricCommand);

    }
}
