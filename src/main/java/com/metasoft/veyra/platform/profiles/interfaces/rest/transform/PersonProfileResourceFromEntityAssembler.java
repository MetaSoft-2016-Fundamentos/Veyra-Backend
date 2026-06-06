package com.metasoft.veyra.platform.profiles.interfaces.rest.transform;

import com.metasoft.veyra.platform.profiles.domain.model.aggregates.PersonProfile;
import com.metasoft.veyra.platform.profiles.interfaces.rest.resources.PersonProfileResource;

public class PersonProfileResourceFromEntityAssembler {
    public static PersonProfileResource toResourceFromEntity(PersonProfile entity){
        return new PersonProfileResource(entity.getId(),entity.getPersonName().getFullName(),entity.getDni().dni(),
                entity.getBirthDate().birthDate(),
                entity.getAge().age(), entity.getPhoto().photoUrl(),entity.getPhoneNumber().phoneNumber(),
                entity.getEmailAddress().emailAddress(),entity.getStreetAddress().getStreetAddress()
        );
    }
}