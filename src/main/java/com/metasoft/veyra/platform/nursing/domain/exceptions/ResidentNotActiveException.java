package com.metasoft.veyra.platform.nursing.domain.exceptions;

/**
 * Exception thrown when a resident is not active.
 * <p>
 * This exception is thrown when an operation requires an active resident
 * but the resident's status is not ACTIVE.
 * </p>
 * @see RuntimeException
 */
public class ResidentNotActiveException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param residentId The ID of the resident that is not active.
     */
    public ResidentNotActiveException(Long residentId) {
        super(String.format("Resident with ID %s is not active.", residentId));
    }
}