package com.metasoft.veyra.platform.tracking.application.internal.queryservices;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Device;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetAllDevicesQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDeviceByIdQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDevicesByNursingHomeIdQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetDevicesByResidentIdQuery;
import com.metasoft.veyra.platform.tracking.domain.model.queries.GetUnassignedDevicesQuery;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.AssignmentStatus;
import com.metasoft.veyra.platform.tracking.domain.services.DeviceQueryService;
import com.metasoft.veyra.platform.tracking.infrastructure.persistence.jpa.repositories.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceQueryServiceImpl implements DeviceQueryService {

    private final DeviceRepository deviceRepository;

    public DeviceQueryServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<Device> handle(GetAllDevicesQuery query) {
        return deviceRepository.findAll();
    }

    @Override
    public Optional<Device> handle(GetDeviceByIdQuery query) {
        return deviceRepository.findById(query.deviceId());
    }

    @Override
    public List<Device> handle(GetDevicesByResidentIdQuery query) {
        return deviceRepository.findAllByResidentId(query.residentId());
    }

    @Override
    public List<Device> handle(GetUnassignedDevicesQuery query) {
        return deviceRepository.findAllByStatus(AssignmentStatus.UNAVAILABLE);
    }

    @Override
    public List<Device> handle(GetDevicesByNursingHomeIdQuery query) {
        return deviceRepository.findAllByNursingHomeId(query.nursingHomeId());
    }
}
