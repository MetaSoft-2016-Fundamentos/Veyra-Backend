package com.metasoft.veyra.platform.nursing.domain.exceptions;

/**
 * Exception thrown when unable to create a business profile.
 * <p>
 * This exception is thrown when an error occurs while creating a business profile
 * in the external profile service.
 * </p>
 * @see RuntimeException
 */
public class BusinessProfileCreationException extends RuntimeException {
    /**
     * Constructor for the exception.
     */
    public BusinessProfileCreationException() {
        super("Unable to create business profile.");
    }
}
