package com.metasoft.veyra.platform.tracking.domain.model.aggregates;

import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.AssignmentStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeviceAggregateTest {

    // ── 1. Constructor simple inicia como UNASSIGNED ──────────────────────────

    @Test
    void shouldInitializeAsUnassignedWhenCreatedWithoutResident() {
        Device device = new Device("BAND-001");

        assertEquals(AssignmentStatus.UNASSIGNED, device.getStatus());
        assertNull(device.getResidentId());
        assertFalse(device.isAssigned(),
                "Un device sin residente no debe considerarse asignado");
    }

    // ── 2. Constructor con residente inicia como ACTIVE ───────────────────────

    @Test
    void shouldInitializeAsActiveWhenCreatedWithResident() {
        Device device = new Device("BAND-002", 5L, "admin@veyra.com");

        assertEquals(AssignmentStatus.ACTIVE, device.getStatus());
        assertEquals(5L, device.getResidentId());
        assertTrue(device.isAssigned());
    }

    // ── 3. assignToResident cambia el estado a ACTIVE ──────────────────────────

    @Test
    void shouldAssignToResidentAndBecomeActive() {
        Device device = new Device("BAND-003");

        device.assignToResident(8L, "supervisor@veyra.com");

        assertEquals(AssignmentStatus.ACTIVE, device.getStatus());
        assertEquals(8L, device.getResidentId());
        assertEquals("supervisor@veyra.com", device.getAssignedBy());
        assertTrue(device.isAssigned());
    }

    // ── 4. unassign limpia el residente y vuelve a UNASSIGNED ─────────────────

    @Test
    void shouldUnassignDeviceCorrectly() {
        Device device = new Device("BAND-004", 1L, "admin@veyra.com");

        device.unassign();

        assertEquals(AssignmentStatus.UNASSIGNED, device.getStatus());
        assertNull(device.getResidentId());
        assertNull(device.getAssignedBy());
        assertFalse(device.isAssigned());
    }

    // ── 5. deactivate cambia el estado a INACTIVE sin tocar al residente ───────

    @Test
    void shouldDeactivateWithoutClearingResident() {
        Device device = new Device("BAND-005", 2L, "admin@veyra.com");

        device.deactivate();

        assertEquals(AssignmentStatus.INACTIVE, device.getStatus());
        assertEquals(2L, device.getResidentId(),
                "deactivate no debe limpiar el residentId");
        assertFalse(device.isAssigned(),
                "isAssigned debe ser false si el status no es ACTIVE, aunque tenga residentId");
    }
}