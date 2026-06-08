package com.metasoft.veyra.platform.nursing.domain.exceptions;

public class RelativeAlreadyExistsException extends RuntimeException {
    public RelativeAlreadyExistsException(Long relativeId) {
        super("Relative with ID " + relativeId + " already exists.");
    }
}
