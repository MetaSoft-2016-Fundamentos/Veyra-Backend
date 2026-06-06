package com.metasoft.veyra.platform.nursing.interfaces.rest.transform;

import com.metasoft.veyra.platform.nursing.domain.model.entities.Room;
import com.metasoft.veyra.platform.nursing.interfaces.rest.resources.RoomResource;

public class RoomResourceFromEntityAssembler {
    public static RoomResource toResourceFromEntity(Room entity)
    {
        return new RoomResource(entity.getId(),entity.getRoomNumber(),entity.getNursingHome().getId(),entity.getRoomOccupancy().capacity(),entity.getType(),entity.getRoomOccupancy().occupied(),entity.getRoomStatus().name());
    }
}
