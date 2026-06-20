package com.metasoft.veyra.platform.hcm.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RegisteredStaffEvent extends ApplicationEvent {
    private final Long staffId;
    private final String email;
    private final String firstName;
    private final String lastName;

    public RegisteredStaffEvent(Object source, Long staffId, String email, String firstName, String lastName) {
        super(source);
        this.staffId = staffId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
