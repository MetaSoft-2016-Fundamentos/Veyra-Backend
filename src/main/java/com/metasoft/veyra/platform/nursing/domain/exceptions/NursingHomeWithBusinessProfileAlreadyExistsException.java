package com.metasoft.veyra.platform.nursing.domain.exceptions;

/**
 * Exception thrown when a nursing home with the same business profile already exists.
 * <p>
 * This exception is thrown when attempting to create a nursing home
 * with a business profile that is already associated with another nursing home.
 * </p>
 * @see RuntimeException
 */
public class NursingHomeWithBusinessProfileAlreadyExistsException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param nursingHomeId The ID of the existing nursing home.
     * @param businessProfileId The ID of the business profile.
     */
    public NursingHomeWithBusinessProfileAlreadyExistsException(Long nursingHomeId, Long businessProfileId) {
        super(String.format("Nursing home with ID %s already exists with business profile ID %s.",
                nursingHomeId, businessProfileId));
    }
}