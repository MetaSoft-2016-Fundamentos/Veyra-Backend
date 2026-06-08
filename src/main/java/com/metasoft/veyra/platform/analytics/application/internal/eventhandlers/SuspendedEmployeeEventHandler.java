package com.metasoft.veyra.platform.analytics.application.internal.eventhandlers;

import com.metasoft.veyra.platform.analytics.domain.model.commands.RecordMetricCommand;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricCategory;
import com.metasoft.veyra.platform.analytics.domain.model.valueobjects.MetricType;
import com.metasoft.veyra.platform.analytics.domain.services.MetricCommandService;
import com.metasoft.veyra.platform.hcm.domain.model.events.EmployeeSuspendedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class SuspendedEmployeeEventHandler {
    private final MetricCommandService metricCommandService;
 private static final Logger LOGGER= LoggerFactory.getLogger(SuspendedEmployeeEventHandler.class);
    public SuspendedEmployeeEventHandler(MetricCommandService metricCommandService) {
        this.metricCommandService = metricCommandService;
    }
    @EventListener
    public void on(EmployeeSuspendedEvent event){
       var nursingHomeId=event.getNursingHomeId().nursingHomeId();
       var employeeId=event.getEmployeeId();
       var contractId=event.getContractId();
       LOGGER.info("Suspending Employee ID:{} in Nursing Home ID:{} with Contract ID:{}",nursingHomeId,employeeId,contractId);
       var command = new RecordMetricCommand(nursingHomeId, MetricType.EMPLOYEE_SUSPENDED, MetricCategory.STAFF,event.getOccurredOn());
       metricCommandService.handle(command);
    }
}
