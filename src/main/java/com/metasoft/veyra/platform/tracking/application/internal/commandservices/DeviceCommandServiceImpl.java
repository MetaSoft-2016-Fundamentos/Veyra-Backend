package com.metasoft.veyra.platform.tracking.application.internal.commandservices;
import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Device;
import com.metasoft.veyra.platform.tracking.domain.model.commands.AssignDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.ChangeDeviceStatusCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.RegisterDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.UnassignDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.UpdateDeviceCommand;
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
    public Long handle(AssignDeviceCommand command) {
        var device = deviceRepository.findById(command.deviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found: " + command.deviceId()));

        device.assignToResident(command.residentId());
        deviceRepository.save(device);

        LOGGER.info("Device {} assigned to resident {} by {}",
                command.deviceId(), command.residentId());

        return device.getId();
    }

    @Override
    public void handle(UnassignDeviceCommand command) {
        var device = deviceRepository.findById(command.deviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found: " + command.deviceId()));

        device.unassign();
        deviceRepository.save(device);

        LOGGER.info("Device {} unassigned", command.deviceId());
    }

    @Override
    public Long handle(RegisterDeviceCommand command) {
        var device = new Device(command.nursingHomeId(), command.deviceType(), command.macAddress());
        deviceRepository.save(device);
        LOGGER.info("Device registered for nursing home {}", command.nursingHomeId());
        return device.getId();
    }

    @Override
    public Long handle(UpdateDeviceCommand command) {
        var device = deviceRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Device not found: " + command.id()));
        device.updateDevice(command.deviceType(),command.macAddress());
        deviceRepository.save(device);
        LOGGER.info("Device {} updated to type {}", command.id(), command.deviceType());
        return device.getId();
    }

    @Override
    public Long handle(ChangeDeviceStatusCommand command) {
        var device = deviceRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Device not found: " + command.id()));
        device.changeStatus(command.status());
        deviceRepository.save(device);
        LOGGER.info("Device {} status changed to {}", command.id(), command.status());
        return device.getId();
    }
}