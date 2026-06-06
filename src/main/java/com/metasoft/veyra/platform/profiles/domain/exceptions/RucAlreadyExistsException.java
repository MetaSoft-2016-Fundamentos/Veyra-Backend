package com.metasoft.veyra.platform.profiles.domain.exceptions;

public class RucAlreadyExistsException extends RuntimeException {
    public RucAlreadyExistsException(String ruc) {

        super(String.format( "RUC '%s' already exists.", ruc));
    }
}
