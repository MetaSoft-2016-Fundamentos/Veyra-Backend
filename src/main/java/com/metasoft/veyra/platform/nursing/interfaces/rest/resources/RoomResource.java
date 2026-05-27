package com.metasoft.veyra.platform.nursing.interfaces.rest.resources;

public record RoomResource(Long id,String roomNumber,Long nursingHomeId, Integer capacity,
                           String type,Integer occupied,String status) {
}
