package com.metasoft.veyra.platform.tracking.integration;

import com.metasoft.veyra.platform.tracking.application.internal.commandservices.DeviceCommandServiceImpl;
import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Device;
import com.metasoft.veyra.platform.tracking.domain.model.commands.AssignDeviceCommand;
import com.metasoft.veyra.platform.tracking.infrastructure.persistence.jpa.repositories.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceCommandServiceImplTest {
    @Mock private DeviceRepository repository;
    @InjectMocks private DeviceCommandServiceImpl service;

    @Test
    void handle_AssignDevice_Exito() {
        Device device = new Device("BAND-001");
        when(repository.findByDeviceId("BAND-001")).thenReturn(Optional.of(device));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.handle(new AssignDeviceCommand("BAND-001", 1L, "Admin"));
        verify(repository, times(1)).save(device);
    }
}