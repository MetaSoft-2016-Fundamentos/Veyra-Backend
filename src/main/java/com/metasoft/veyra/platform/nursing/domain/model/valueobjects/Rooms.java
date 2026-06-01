package com.metasoft.veyra.platform.nursing.domain.model.valueobjects;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.NursingHome;
import com.metasoft.veyra.platform.nursing.domain.model.entities.Room;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

/**
 * Rooms value object.
 * <p>
 * This value object represents a collection of rooms in a nursing home.
 * A rooms collection manages all rooms within a nursing home.
 * </p>
 * @see Room
 * @since 1.0
 */
@Embeddable
public class Rooms {

    @OneToMany(mappedBy = "nursingHome", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

    /**
     * Default constructor.
     * <p>
     * Initialize the rooms collection with an empty list.
     * </p>
     */
    public Rooms() {
        this.rooms = new ArrayList<>();
    }

    /**
     * Add a new room to the nursing home.
     * @param nursingHome the nursing home
     * @param capacity the capacity of the room
     * @param type the type of the room
     */
    public void addRoom(NursingHome nursingHome, Integer capacity, String type,String roomNumber) {
        if (existsRoomNumber(roomNumber)) {
            throw new IllegalArgumentException(
                    String.format("Room number %s already exists in this nursing home", roomNumber)
            );
        }
        Room room = new Room(nursingHome, capacity, type,roomNumber);
        this.rooms.add(room);
    }

    /**
     * Get a room by its room number.
     * @param roomNumber the room number
     * @return the room with the given room number, or null if not found
     */
    public Room getRoomByRoomNumber(String roomNumber) {
        return rooms.stream()
                .filter(room -> room.getRoomNumber().equals(roomNumber))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get a room by its ID.
     * @param roomId the room ID
     * @return the room with the given ID, or null if not found
     */
    public Room getRoomById(Long roomId) {
        return rooms.stream()
                .filter(room -> room.getId().equals(roomId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all available rooms.
     * @return list of available rooms
     */
    public List<Room> getAvailableRooms() {
        return rooms.stream()
                .filter(Room::isAvailable)
                .toList();
    }

    /**
     * Check if the rooms collection is empty.
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return rooms.isEmpty();
    }

    /**
     * Get all rooms.
     * @return list of all rooms
     */
    public List<Room> getAllRooms() {
        return rooms;
    }


    public Room getLastAddedRoom() {
        if (rooms.isEmpty()) {
            return null;
        }
        return rooms.getLast();
    }

    /**
     * Check if a room number already exists in this nursing home.
     * @param roomNumber the room number to check
     * @return true if exists, false otherwise
     */
    public boolean existsRoomNumber(String roomNumber) {
        return rooms.stream()
                .anyMatch(room -> room.getRoomNumber().equals(roomNumber));
    }
    /**
     * Get rooms by status.
     * @param status the room status to filter by
     * @return list of rooms with the given status
     */
    public List<Room> getRoomsByStatus(RoomStatus status) {
        return rooms.stream()
                .filter(room -> room.getRoomStatus() == status)
                .toList();
    }
}