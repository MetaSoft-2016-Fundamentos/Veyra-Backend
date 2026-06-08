package com.metasoft.veyra.platform.tracking.application.internal.commandservices;

import com.metasoft.veyra.platform.tracking.domain.model.aggregates.Measurement;
import com.metasoft.veyra.platform.tracking.domain.model.commands.SeedMeasurementCommand;
import com.metasoft.veyra.platform.tracking.domain.model.events.MeasurementReceivedEvent;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.*;
import com.metasoft.veyra.platform.tracking.domain.services.MeasurementCommandService;
import com.metasoft.veyra.platform.tracking.infrastructure.persistence.mongodb.repositories.MeasurementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MeasurementCommandServiceImpl implements MeasurementCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementCommandServiceImpl.class);

    private final ApplicationEventPublisher eventPublisher;
    private final MeasurementRepository measurementRepository;

    public MeasurementCommandServiceImpl(
            ApplicationEventPublisher eventPublisher,
            MeasurementRepository measurementRepository) {
        this.eventPublisher = eventPublisher;
        this.measurementRepository = measurementRepository;
    }

    @Override
    @Transactional
    public void handle(SeedMeasurementCommand command) {
        if (measurementRepository.count() > 0) {
            LOGGER.info("Measurements already seeded. Skipping...");
            return;
        }

        LOGGER.info("Starting measurements seeding...");
        var measurements = generateMeasurements();

        int eventCount = 0;
        for (Measurement measurement : measurements) {
            measurementRepository.save(measurement);

            publishMeasurementEvent(measurement);
            eventCount++;
        }

        LOGGER.info("Seeded {} measurements and published {} events", measurements.size(), eventCount);
    }

    private void publishMeasurementEvent(Measurement measurement) {
        var event = new MeasurementReceivedEvent(
                this,
                measurement.getId(),
                measurement.getDeviceId().deviceId(),
                measurement.getTimestamp(),
                measurement.getHeartRate() != null ? measurement.getHeartRate().value() : null,
                measurement.getBloodPressure() != null ? measurement.getBloodPressure().systolic() : null,
                measurement.getBloodPressure() != null ? measurement.getBloodPressure().diastolic() : null,
                measurement.getTemperature() != null ? measurement.getTemperature().value() : null,
                measurement.getOxygenSaturation() != null ? measurement.getOxygenSaturation().value() : null,
                measurement.getRespiratoryRate() != null ? measurement.getRespiratoryRate().value() : null
        );

        eventPublisher.publishEvent(event);

        LOGGER.debug("Published MeasurementReceivedEvent for device {} at {}",
                measurement.getDeviceId().deviceId(),
                measurement.getTimestamp());
    }

    private List<Measurement> generateMeasurements() {
        var measurements = new ArrayList<Measurement>();
        var random = new Random();
        var totalDevices = 10;
        var measurementsPerDevice = 24;

        for (int deviceNum = 1; deviceNum <= totalDevices; deviceNum++) {
            var deviceId = String.format("BAND-%03d", deviceNum);

            for (int hour = 0; hour < measurementsPerDevice; hour++) {
                var timestamp = LocalDateTime.now().minusHours(measurementsPerDevice - hour);
                var generateAbnormal = (hour % 6 == 0);

                var measurement = createMeasurement(
                        deviceId,
                        timestamp,
                        generateAbnormal,
                        random
                );

                measurements.add(measurement);
            }
        }

        return measurements;
    }

    private Measurement createMeasurement(
            String deviceId,
            LocalDateTime timestamp,
            boolean abnormal,
            Random random) {

        var heartRate = generateHeartRate(abnormal, random);
        var systolic = generateSystolic(abnormal, random);
        var diastolic = generateDiastolic(abnormal, random);

        if (systolic <= diastolic) {
            systolic = diastolic + 20;
        }

        var temperature = generateTemperature(abnormal, random);
        var oxygenSaturation = generateOxygenSaturation(abnormal, random);
        var respiratoryRate = generateRespiratoryRate(abnormal, random);

        return new Measurement(
                new DeviceId(deviceId),
                timestamp,
                new HeartRate(heartRate),
                new BloodPressure(systolic, diastolic),
                new Temperature(temperature),
                new OxygenSaturation(oxygenSaturation),
                new RespiratoryRate(respiratoryRate)
        );
    }

    private int generateHeartRate(boolean abnormal, Random random) {
        return abnormal
                ? (random.nextBoolean() ? 45 + random.nextInt(10) : 110 + random.nextInt(20))
                : 60 + random.nextInt(35);
    }

    private int generateSystolic(boolean abnormal, Random random) {
        return abnormal
                ? (random.nextBoolean() ? 160 + random.nextInt(25) : 85 + random.nextInt(15))
                : 110 + random.nextInt(30);
    }

    private int generateDiastolic(boolean abnormal, Random random) {
        return abnormal
                ? (random.nextBoolean() ? 95 + random.nextInt(15) : 50 + random.nextInt(15))
                : 70 + random.nextInt(20);
    }

    private double generateTemperature(boolean abnormal, Random random) {
        var temp = abnormal
                ? (random.nextBoolean() ? 38.5 + random.nextDouble() * 1.5 : 35.0 + random.nextDouble() * 0.8)
                : 36.1 + random.nextDouble() * 1.2;
        return Math.round(temp * 10.0) / 10.0;
    }

    private int generateOxygenSaturation(boolean abnormal, Random random) {
        return abnormal
                ? 82 + random.nextInt(10)
                : 95 + random.nextInt(5);
    }

    private int generateRespiratoryRate(boolean abnormal, Random random) {
        return abnormal
                ? (random.nextBoolean() ? 8 + random.nextInt(4) : 24 + random.nextInt(8))
                : 12 + random.nextInt(8);
    }
}