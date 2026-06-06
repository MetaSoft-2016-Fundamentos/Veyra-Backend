package com.metasoft.veyra.platform.nursing.domain.exceptions;

/**
 * Exception thrown when an error occurs while deleting a resident.
 * <p>
 * This exception is thrown when an error occurs during the resident deletion process.
 * </p>
 * @see RuntimeException
 */
public class ResidentDeletionException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param exceptionMessage The message of the exception.
     */
    public ResidentDeletionException(String exceptionMessage) {
        super("Error while deleting resident: %s".formatted(exceptionMessage));
    }
}