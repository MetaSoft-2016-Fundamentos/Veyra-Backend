package com.metasoft.veyra.platform.tracking.application.internal.commandservices;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.GpsLocation;
import com.metasoft.veyra.platform.tracking.domain.model.commands.RecordLocationCommand;
import com.metasoft.veyra.platform.tracking.domain.services.LocationCommandService;
import com.metasoft.veyra.platform.tracking.infrastructure.persistence.mongodb.repositories.GpsLocationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class LocationCommandServiceImpl implements LocationCommandService {

    private final GpsLocationRepository gpsLocationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public LocationCommandServiceImpl(GpsLocationRepository gpsLocationRepository,
                                      SimpMessagingTemplate messagingTemplate) {
        this.gpsLocationRepository = gpsLocationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public GpsLocation handle(RecordLocationCommand command) {
        var location = new GpsLocation(command.deviceId(), command.latitude(), command.longitude());
        gpsLocationRepository.save(location);
        messagingTemplate.convertAndSend("/topic/tracking/" + command.deviceId(), location);
        return location;
    }
}
