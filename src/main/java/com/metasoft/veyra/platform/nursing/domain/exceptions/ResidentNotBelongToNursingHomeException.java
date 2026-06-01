package com.metasoft.veyra.platform.nursing.domain.exceptions;


/**
 * Exception thrown when a resident does not belong to a nursing home.
 * <p>
 * This exception is thrown when an operation requires that the resident
 * belongs to a specific nursing home, but it doesn't.
 * </p>
 * @see RuntimeException
 */
public class ResidentNotBelongToNursingHomeException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param residentId The ID of the resident.
     * @param nursingHomeId The ID of the nursing home.
     */
    public ResidentNotBelongToNursingHomeException(Long residentId, Long nursingHomeId) {
        super(String.format("Resident with ID %s does not belong to nursing home with ID %s.",
                residentId, nursingHomeId));
    }
}