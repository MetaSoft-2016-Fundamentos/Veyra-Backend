package com.metasoft.veyra.platform.nursing.application.internal.queryservices;

import com.metasoft.veyra.platform.nursing.domain.model.aggregates.NursingHome;
import com.metasoft.veyra.platform.nursing.domain.model.entities.Room;
import com.metasoft.veyra.platform.nursing.domain.model.queries.*;
import com.metasoft.veyra.platform.nursing.domain.services.NursingHomeQueryServices;
import com.metasoft.veyra.platform.nursing.infrastructure.persistence.jpa.repositories.NursingHomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NursingHomeQueryServiceImpl implements NursingHomeQueryServices {
    private final NursingHomeRepository nursingHomeRepository;

    public NursingHomeQueryServiceImpl(NursingHomeRepository nursingHomeRepository) {
        this.nursingHomeRepository = nursingHomeRepository;

    }

    @Override
    public Optional<NursingHome> handle(GetNursingHomeByIdQuery query) {
        return nursingHomeRepository.findById(query.id());
    }

    @Override
    public List<NursingHome> handle(GetAllNursingHomeQuery query) {
        return nursingHomeRepository.findAll();
    }

    @Override
    public boolean handle(ExistsByNursingHomeIdQuery query) {
        return nursingHomeRepository.existsById(query.nursingHomeId());
    }

    @Override
    public List<Room> handle(GetRoomsForNursingHomeIdQuery query) {
        return nursingHomeRepository.findById(query.nursingHomeId())
                .map(nursingHome -> nursingHome.getRooms().getAllRooms())
                .orElseThrow(() -> new IllegalArgumentException(
                        "NursingHome not found with id: " + query.nursingHomeId()));
    }

    @Override
    public Optional<Room> handle(GetRoomByNursingHomeIdAndRoomNumberQuery query) {
        return nursingHomeRepository.findById(query.nursingHomeId()).map(nursingHome -> nursingHome.getRooms().getRoomByRoomNumber(query.roomNumber()));
    }

    @Override
    public Optional<Room> handle(GetLastAddedRoomByNursingHomeIdQuery query) {
        return nursingHomeRepository.findById(query.nursingHomeId())
                .map(nursingHome -> nursingHome.getRooms().getLastAddedRoom());
    }

    @Override
    public List<Room> handle(GetRoomsByStatusAndNursingHomeIdQuery query) {
        return nursingHomeRepository.findById(query.nursingHomeId())
                .map(nursingHome -> nursingHome.getRooms().getRoomsByStatus(query.roomStatus()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "NursingHome not found with id: " + query.nursingHomeId()));
    }

    @Override
    public Optional<NursingHome> handle(GetNursingHomeByAdministratorIdQuery query) {
        return nursingHomeRepository.findByAdministratorId(query.administratorId());
    }
}
