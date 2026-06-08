package com.metasoft.veyra.platform.nursing.domain.exceptions;


/**
 * Exception thrown when a resident is already in the specified room.
 * <p>
 * This exception is thrown when attempting to change a resident's room
 * to the same room they are currently in.
 * </p>
 * @see RuntimeException
 */
public class ResidentAlreadyInRoomException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param residentId The ID of the resident.
     * @param roomNumber The number of the room.
     */
    public ResidentAlreadyInRoomException(Long residentId, String roomNumber) {
        super(String.format("Resident with ID %s is already in room %s.", residentId, roomNumber));
    }
}
