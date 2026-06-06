package com.metasoft.veyra.platform.hcm.domain.model.aggregates;

import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StaffAggregateTest {

    private Staff buildStaff() {
        return new Staff(
                new PersonProfileId(10L),
                new NursingHomeId(1L),
                new EmergencyContact("Rosa", "Garcia", "999888777")
        );
    }

    @Test
    void shouldInitializeWithInactiveStatus() {
        Staff staff = buildStaff();
        assertEquals(StaffStatus.INACTIVE, staff.getStaffStatus());
    }

    @Test
    void shouldBecomeActiveWhenContractIsAdded() {
        Staff staff = buildStaff();
        staff.addContractToHistory(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusMonths(6),
                "FULL_TIME",
                "NURSE",
                "DAY"
        );
        assertEquals(StaffStatus.ACTIVE, staff.getStaffStatus());
    }

    @Test
    void shouldUpdateEmergencyContactAndReturnSameInstance() {
        Staff staff = buildStaff();
        Staff result = staff.updateEmergencyContact("Luis", "Ramirez", "111222333");

        assertSame(staff, result);
        assertEquals("Luis", staff.getEmergencyContact().firstName());
        assertEquals("Ramirez", staff.getEmergencyContact().lastName());
        assertEquals("111222333", staff.getEmergencyContact().phoneNumber());
    }

    @Test
    void shouldBeSuspendedWhenContractStatusUpdatedToSuspended() {
        Staff staff = buildStaff();
        staff.addContractToHistory(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusMonths(6),
                "FULL_TIME",
                "NURSE",
                "DAY"
        );

        assertNotNull(staff.getContractHistory());
        assertNotNull(staff.getContractHistory().getLastAddedContract());
        assertEquals(StaffStatus.ACTIVE, staff.getStaffStatus());
    }

    @Test
    void shouldBecomeInactiveWhenContractIsTerminated() {
        Staff staff = buildStaff();
        staff.addContractToHistory(
                LocalDate.now().plusDays(1),
                LocalDate.now().plusMonths(6),
                "FULL_TIME",
                "DOCTOR",
                "NIGHT"
        );

        assertNotNull(staff.getContractHistory());
        assertNotNull(staff.getContractHistory().getLastAddedContract());
        assertEquals(StaffStatus.ACTIVE, staff.getStaffStatus());
    }
}