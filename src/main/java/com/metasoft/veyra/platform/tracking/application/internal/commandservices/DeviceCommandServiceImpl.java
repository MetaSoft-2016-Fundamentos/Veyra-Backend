package com.metasoft.veyra.platform.tracking.application.internal.commandservices;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Device;
import com.metasoft.veyra.platform.tracking.domain.model.commands.AssignDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.SeedDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.UnassignDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceCommandService;
import com.metasoft.veyra.platform.tracking.infrastructure.persistence.jpa.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceCommandServiceImpl.class);

    private final DeviceRepository deviceRepository;

    public DeviceCommandServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public void handle(SeedDeviceCommand command) {
        if (deviceRepository.count() > 0) {
            LOGGER.info("Devices already seeded. Skipping...");
            return;
        }
        LOGGER.info("Starting devices seeding...");
        for (int i = 1; i <= 10; i++) {
            String deviceId = String.format("BAND-%03d", i);
            Device device = new Device(deviceId, (long) i, "SYSTEM_SEED");
            deviceRepository.save(device);
            LOGGER.debug("Created device {} assigned to resident {}", deviceId, i);
        }

        LOGGER.info("Seeded 10 devices (8 assigned, 2 unassigned)");
    }

    @Override
    public Long handle(AssignDeviceCommand command) {
        var device = deviceRepository.findByDeviceId(command.deviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found: " + command.deviceId()));

        device.assignToResident(command.residentId(), command.assignedBy());
        deviceRepository.save(device);

        LOGGER.info("Device {} assigned to resident {} by {}",
                command.deviceId(), command.residentId(), command.assignedBy());

        return device.getId();
    }

    @Override
    public void handle(UnassignDeviceCommand command) {
        var device = deviceRepository.findByDeviceId(command.deviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found: " + command.deviceId()));

        device.unassign();
        deviceRepository.save(device);

        LOGGER.info("Device {} unassigned", command.deviceId());
    }
}