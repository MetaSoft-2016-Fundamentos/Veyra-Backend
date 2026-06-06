package com.metasoft.veyra.platform.nursing.domain.exceptions;

/**
 * Exception thrown when an error occurs while updating a resident.
 * <p>
 * This exception is thrown when an error occurs during the resident update process.
 * </p>
 * @see RuntimeException
 */
public class ResidentUpdateException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param exceptionMessage The message of the exception.
     */
    public ResidentUpdateException(String exceptionMessage) {
        super("Error while updating resident: %s".formatted(exceptionMessage));
    }
}