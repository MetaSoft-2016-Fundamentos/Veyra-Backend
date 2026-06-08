package com.metasoft.veyra.platform.nursing.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record RoomOccupancy(Integer capacity, Integer occupied) {

    public RoomOccupancy {
        if (capacity == null || capacity <= 0)
            throw new IllegalArgumentException("Capacity must be greater than zero");

        if (occupied == null || occupied < 0)
            throw new IllegalArgumentException("Occupied must be zero or more");

        if (occupied > capacity)
            throw new IllegalArgumentException("Occupied cannot exceed capacity");
    }

    public boolean isFull() {
        return occupied.equals(capacity);
    }

    public int availableSlots() {
        return capacity - occupied;
    }

    public RoomOccupancy incrementOccupiedSlots(int amount) {
        return new RoomOccupancy(capacity, occupied + amount);
    }

    public RoomOccupancy release(int amount) {
        return new RoomOccupancy(capacity, occupied - amount);
    }
}

