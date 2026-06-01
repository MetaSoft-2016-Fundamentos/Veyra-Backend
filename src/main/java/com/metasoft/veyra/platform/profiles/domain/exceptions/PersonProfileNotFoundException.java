package com.metasoft.veyra.platform.profiles.domain.exceptions;

public class PersonProfileNotFoundException extends RuntimeException {
    public PersonProfileNotFoundException(Long personProfileId)
    {
        super(String.format("Person profile with ID '%d' not found.", personProfileId));
    }
}
