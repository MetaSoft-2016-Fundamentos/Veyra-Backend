package com.metasoft.veyra.platform.nursing.domain.model.queries;

import com.metasoft.veyra.platform.nursing.domain.model.valueobjects.RoomStatus;

public record GetRoomsByStatusAndNursingHomeIdQuery(RoomStatus roomStatus,Long nursingHomeId) {
    public GetRoomsByStatusAndNursingHomeIdQuery{
        if (nursingHomeId==null){
            throw new IllegalArgumentException("Nursing Home id cannot be null");
        }
        if (nursingHomeId<1){
            throw new IllegalArgumentException("Nursing Home id must be less then 1 ");
        }
    }
}
