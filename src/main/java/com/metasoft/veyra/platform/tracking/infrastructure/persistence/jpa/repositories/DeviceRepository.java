package com.metasoft.veyra.platform.tracking.infrastructure.persistence.jpa.repositories;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Device;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.AssignmentStatus;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.MacAddress;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.NursingHomeId;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.ResidentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device,Long>
{
    Optional<Device> findByMacAddress(MacAddress macAddress);
    List<Device> findAllByStatus(AssignmentStatus status);
    List<Device> findAllByResidentId(ResidentId residentId);
    List<Device> findAllByNursingHomeId(NursingHomeId nursingHomeId);
    boolean existsByMacAddress(MacAddress macAddress);
    Optional<Device> findByMacAddressAndStatus(MacAddress macAddress, AssignmentStatus status);

    boolean existsDeviceById(Long id);
}
