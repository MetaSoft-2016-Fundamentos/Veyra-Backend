package com.metasoft.veyra.platform.nursing.domain.model.queries;

public record GetRoomsForNursingHomeIdQuery(Long nursingHomeId) {
    public GetRoomsForNursingHomeIdQuery{
        if (nursingHomeId==null||nursingHomeId<1)
        {
            throw new IllegalArgumentException("nursingHomeId invalid");
        }
    }
}
