package com.metasoft.veyra.platform.tracking.application.internal.commandservices;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Device;
import com.metasoft.veyra.platform.tracking.domain.model.commands.AssignDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.SeedDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.commands.UnassignDeviceCommand;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.AssignmentStatus;
import com.metasoft.veyra.platform.tracking.infrastructure.persistence.jpa.repositories.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DeviceCommandServiceImplTest {

    @Mock private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceCommandServiceImpl deviceCommandService;

    // ── 1. Asignar un device existente a un residente ─────────────────────────

    @Test
    void shouldAssignDeviceToResidentWhenDeviceExists() {
        Device device = new Device("BAND-010");
        AssignDeviceCommand command = new AssignDeviceCommand("BAND-010", 7L, "admin@veyra.com");

        when(deviceRepository.findByDeviceId("BAND-010")).thenReturn(Optional.of(device));
        when(deviceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        deviceCommandService.handle(command);

        assertEquals(AssignmentStatus.ACTIVE, device.getStatus());
        assertEquals(7L, device.getResidentId());
        verify(deviceRepository).save(device);
    }

    // ── 2. Lanza excepción si el device no existe al asignar ──────────────────

    @Test
    void shouldThrowWhenAssigningNonExistentDevice() {
        AssignDeviceCommand command = new AssignDeviceCommand("BAND-999", 7L, "admin@veyra.com");
        when(deviceRepository.findByDeviceId("BAND-999")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> deviceCommandService.handle(command));

        assertTrue(ex.getMessage().contains("BAND-999"));
        verify(deviceRepository, never()).save(any());
    }

    // ── 3. Desasignar un device existente ──────────────────────────────────────

    @Test
    void shouldUnassignExistingDevice() {
        Device device = new Device("BAND-011", 3L, "admin@veyra.com");
        UnassignDeviceCommand command = new UnassignDeviceCommand("BAND-011");

        when(deviceRepository.findByDeviceId("BAND-011")).thenReturn(Optional.of(device));

        deviceCommandService.handle(command);

        assertEquals(AssignmentStatus.UNASSIGNED, device.getStatus());
        assertNull(device.getResidentId());
        verify(deviceRepository).save(device);
    }

    // ── 4. Seed no hace nada si ya existen devices ────────────────────────────

    @Test
    void shouldSkipSeedingWhenDevicesAlreadyExist() {
        when(deviceRepository.count()).thenReturn(5L);

        deviceCommandService.handle(new SeedDeviceCommand());

        verify(deviceRepository, never()).save(any());
    }

    // ── 5. Seed crea 10 devices si la tabla está vacía ────────────────────────

    @Test
    void shouldSeedTenDevicesWhenRepositoryIsEmpty() {
        when(deviceRepository.count()).thenReturn(0L);

        deviceCommandService.handle(new SeedDeviceCommand());

        verify(deviceRepository, times(10)).save(any(Device.class));
    }
}