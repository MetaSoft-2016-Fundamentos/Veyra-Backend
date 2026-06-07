package com.metasoft.veyra.platform.tracking.aggregates;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Device;
import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Measurement;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TrackingDomainTest {

    @Nested
    @DisplayName("Tests para el Agregado Device")
    class DeviceAggregateTests {
        @Test
        @DisplayName("Debe asignar dispositivo exitosamente")
        void testAssign_Exito() {
            Device device = new Device("BAND-001");
            device.assignToResident(1L, "Admin");
            assertThat(device.getStatus()).isEqualTo(AssignmentStatus.ACTIVE);
            assertThat(device.isAssigned()).isTrue();
        }
    }

    @Nested
    @DisplayName("Tests para el Agregado Measurement")
    class MeasurementAggregateTests {
        @Test
        @DisplayName("Debe crear medición correctamente")
        void testCreateMeasurement_Exito() {
            Measurement m = new Measurement(new DeviceId("BAND-001"), LocalDateTime.now(),
                    new HeartRate(75), new BloodPressure(120, 80), new Temperature(36.5),
                    new OxygenSaturation(98), new RespiratoryRate(16));

            assertThat(m.getDeviceId().deviceId()).isEqualTo("BAND-001");
            assertThat(m.getHeartRate().value()).isEqualTo(75);
        }
    }
}