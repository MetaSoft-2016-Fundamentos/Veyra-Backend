package com.metasoft.veyra.platform.nursing.domain.exceptions;

public class AdministratorNotFoundException extends RuntimeException {
    public AdministratorNotFoundException(Long administratorId) {
        super(String.format("Administrator with id %d not found.", administratorId));
    }
}
