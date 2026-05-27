package com.metasoft.veyra.platform.hcm.domain.model.events;

import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.NursingHomeId;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

@Getter
public class EmployeeTerminationEvent extends ApplicationEvent {
    private final NursingHomeId nursingHomeId;
    private  final Long employeeId;
    private final Long contractId;
    private final String staffRole;
    private final String typeOfContract;
    private final LocalDate occurredOn;
    public EmployeeTerminationEvent(Object source,  Long employeeId, Long contractId, NursingHomeId nursingHomeId,String staffRole, String typeOfContract, LocalDate occurredOn) {
        super(source);
        this.nursingHomeId = nursingHomeId;
        this.employeeId = employeeId;
        this.contractId = contractId;
        this.staffRole = staffRole;
        this.typeOfContract = typeOfContract;
        this.occurredOn = occurredOn;
    }
}
