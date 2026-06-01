package com.metasoft.veyra.platform.nursing.domain.exceptions;

/**
 * Exception thrown when a medication already exists for a resident.
 * <p>
 * This exception is thrown when attempting to create a medication
 * that already exists for a specific resident.
 * </p>
 * @see RuntimeException
 */
public class MedicationAlreadyExistsException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param medicationName The name of the medication that already exists.
     * @param residentId The ID of the resident.
     */
    public MedicationAlreadyExistsException(String medicationName, Long residentId) {
        super(String.format("Medication '%s' already exists for resident with ID %s.",
                medicationName, residentId));
    }
}