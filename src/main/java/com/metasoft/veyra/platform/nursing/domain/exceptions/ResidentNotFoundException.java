package com.metasoft.veyra.platform.nursing.domain.exceptions;

/**
 * Exception thrown when a resident is not found.
 * <p>
 * This exception is thrown when a resident is not found in the database.
 * </p>
 * @see RuntimeException
 */
public class ResidentNotFoundException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param residentId The ID of the resident that was not found.
     */
    public ResidentNotFoundException(Long residentId) {
        super(String.format("Resident with ID %s not found.", residentId));
    }
}
