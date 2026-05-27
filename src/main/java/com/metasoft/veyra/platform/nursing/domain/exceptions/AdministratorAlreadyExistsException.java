package com.metasoft.veyra.platform.nursing.domain.exceptions;

public class AdministratorAlreadyExistsException extends RuntimeException {
    public AdministratorAlreadyExistsException(Long userId) {
        super("Administrator with user id " + userId + " already exists.");
    }
}
