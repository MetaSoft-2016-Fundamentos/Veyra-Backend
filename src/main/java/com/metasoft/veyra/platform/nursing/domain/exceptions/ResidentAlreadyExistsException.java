package com.metasoft.veyra.platform.nursing.domain.exceptions;

/**
 * Exception thrown when a resident already exists in a nursing home.
 * <p>
 * This exception is thrown when attempting to create a resident
 * that already exists in the specified nursing home.
 * </p>
 * @see RuntimeException
 */
public class ResidentAlreadyExistsException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param nursingHomeId The ID of the nursing home.
     * @param personProfileId The ID of the person profile.
     */
    public ResidentAlreadyExistsException(Long nursingHomeId, Long personProfileId) {
        super(String.format("Resident with person profile ID %s already exists in nursing home with ID %s.",
                personProfileId, nursingHomeId));
    }
}