package com.metasoft.veyra.platform.nursing.domain.services;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.NursingHome;
import com.metasoft.veyra.platform.nursing.domain.model.entities.Room;
import com.metasoft.veyra.platform.nursing.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface NursingHomeQueryServices {
    Optional<NursingHome>handle(GetNursingHomeByIdQuery query);
    List<NursingHome>handle(GetAllNursingHomeQuery query);
    boolean handle(ExistsByNursingHomeIdQuery query);
    List<Room>handle(GetRoomsForNursingHomeIdQuery query);
    Optional<Room> handle(GetRoomByNursingHomeIdAndRoomNumberQuery query);
    Optional<Room>handle(GetLastAddedRoomByNursingHomeIdQuery query);
    List<Room>handle(GetRoomsByStatusAndNursingHomeIdQuery query);
    Optional<NursingHome>handle(GetNursingHomeByAdministratorIdQuery query);
}
