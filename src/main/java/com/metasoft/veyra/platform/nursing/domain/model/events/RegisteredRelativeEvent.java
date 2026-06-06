package com.metasoft.veyra.platform.nursing.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class RegisteredRelativeEvent extends ApplicationEvent {
    private final String email;
    private final String firstName;
    private final String lastName;
    public RegisteredRelativeEvent(Object source, String email, String firstName, String lastName) {
        super(source);
        this.email=email;
        this.firstName=firstName;
        this.lastName=lastName;
    }
}
