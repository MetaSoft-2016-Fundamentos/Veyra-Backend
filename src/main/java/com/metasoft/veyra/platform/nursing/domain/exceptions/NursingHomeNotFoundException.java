package com.metasoft.veyra.platform.nursing.domain.exceptions;

public class NursingHomeNotFoundException extends RuntimeException {
    public NursingHomeNotFoundException(Long nursingHomeId) {
        super(String.format("Nursing home with id %d not found", nursingHomeId));
    }
}
