package com.metasoft.veyra.platform.tracking.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Device;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device,Long>
{
    Optional<Device> findByDeviceId(String deviceId);
    List<Device> findAllByStatus(AssignmentStatus status);
    List<Device> findAllByResidentId(Long residentId);
    boolean existsByDeviceId(String deviceId);
    Optional<Device> findByDeviceIdAndStatus(String deviceId, AssignmentStatus status);
}
