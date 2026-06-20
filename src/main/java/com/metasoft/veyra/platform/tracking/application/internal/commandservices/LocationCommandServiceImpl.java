package com.metasoft.veyra.platform.tracking.application.internal.commandservices;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Location;
import com.metasoft.veyra.platform.tracking.domain.model.commands.RecordLocationCommand;
import com.metasoft.veyra.platform.tracking.domain.services.LocationCommandService;
import com.metasoft.veyra.platform.tracking.infrastructure.persistence.jpa.repositories.DeviceRepository;
import com.metasoft.veyra.platform.tracking.infrastructure.persistence.mongodb.repositories.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class LocationCommandServiceImpl implements LocationCommandService {

    private final LocationRepository locationRepository;
    private final DeviceRepository deviceRepository;

    public LocationCommandServiceImpl(LocationRepository gpsLocationRepository, DeviceRepository deviceRepository) {
        this.locationRepository = gpsLocationRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Optional<Location> handle(RecordLocationCommand command) {
        if (!deviceRepository.existsDeviceById(command.deviceId())){
            throw new IllegalArgumentException("device dont exists");
        }
        var location = new Location(command.deviceId(), command.latitude(), command.longitude());
        var saveLocation=locationRepository.save(location);
        return Optional.of(saveLocation);

    }
}
