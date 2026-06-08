package com.metasoft.veyra.platform.nursing.domain.exceptions;

/**
 * Exception thrown when a room is not found.
 * <p>
 * This exception is thrown when a room is not found in a nursing home.
 * </p>
 * @see RuntimeException
 */
public class RoomNotFoundException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param roomNumber The number of the room that was not found.
     */
    public RoomNotFoundException(String roomNumber) {
        super(String.format("Room with number %s not found.", roomNumber));
    }
}
