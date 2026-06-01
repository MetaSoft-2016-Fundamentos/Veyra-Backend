package com.metasoft.veyra.platform.nursing.domain.exceptions;

/**
 * Exception thrown when an error occurs while adding a room to a nursing home.
 * <p>
 * This exception is thrown when an error occurs during the room creation process.
 * </p>
 * @see RuntimeException
 */
public class RoomCreationException extends RuntimeException {
    /**
     * Constructor for the exception.
     * @param exceptionMessage The message of the exception.
     */
    public RoomCreationException(String exceptionMessage) {
        super("Error while adding room: %s".formatted(exceptionMessage));
    }
}