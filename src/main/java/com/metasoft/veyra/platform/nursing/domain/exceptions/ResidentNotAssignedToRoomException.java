package com.metasoft.veyra.platform.nursing.domain.exceptions;
/**
 * Exception thrown when a resident is not assigned to any room.
 * <p>
 * This exception is thrown when an operation requires the resident
 * to be assigned to a room ,but it isn't.
 * </p>
 * @see RuntimeException
 */
public class ResidentNotAssignedToRoomException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param residentId The ID of the resident.
     */
    public ResidentNotAssignedToRoomException(Long residentId) {
        super(String.format("Resident with ID %s is not currently assigned to any room.", residentId));
    }
}
