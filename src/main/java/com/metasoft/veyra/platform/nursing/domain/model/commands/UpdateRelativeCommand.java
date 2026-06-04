package com.metasoft.veyra.platform.nursing.domain.model.commands;

import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailAddress;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.PersonName;

public record UpdateRelativeCommand(Long id, PersonName personName,EmailAddress emailAddress , Long residentId) {

    public UpdateRelativeCommand{
        if(id== null){
            throw new IllegalArgumentException("Id cannot be null");
        }
        if(emailAddress == null || emailAddress.emailAddress().isBlank()){
            throw new IllegalArgumentException("Email address cannot be null or blank");
        }
        if(personName == null || personName.firstName().isBlank() || personName.lastName().isBlank()){
            throw new IllegalArgumentException("First name and last name cannot be null or blank");
        }

    }
}