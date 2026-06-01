package com.metasoft.veyra.platform.nursing.domain.exceptions;

/**
 * Exception thrown when an error occurs while creating a medication.
 * <p>
 * This exception is thrown when an error occurs while creating or saving a medication.
 * </p>
 * @see RuntimeException
 */
public class MedicationCreationException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param exceptionMessage The message of the exception.
     */
    public MedicationCreationException(String exceptionMessage) {
        super("Error while creating the medication: %s".formatted(exceptionMessage));
    }
}