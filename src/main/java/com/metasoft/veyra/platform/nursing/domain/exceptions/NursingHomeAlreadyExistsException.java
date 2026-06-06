package com.metasoft.veyra.platform.nursing.domain.exceptions;

public class NursingHomeAlreadyExistsException extends RuntimeException {
    public NursingHomeAlreadyExistsException(Long  administratorId) {
        super(String.format("Nursing home for administrator with id %d already exists.", administratorId));
    }
}
