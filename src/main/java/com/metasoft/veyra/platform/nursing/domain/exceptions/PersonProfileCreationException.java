package com.metasoft.veyra.platform.nursing.domain.exceptions;

/**
 * Exception thrown when unable to create a person profile.
 * <p>
 * This exception is thrown when an error occurs while creating a person profile
 * in the external profile service.
 * </p>
 * @see RuntimeException
 */
public class PersonProfileCreationException extends RuntimeException {
    /**
     * Constructor for the exception.
     */
    public PersonProfileCreationException() {
        super("Unable to create person profile.");
    }
}