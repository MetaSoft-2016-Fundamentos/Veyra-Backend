package com.metasoft.veyra.platform.health.application.internal.commandservices;

import com.metasoft.veyra.platform.health.application.internal.outboundservices.acl.ExternalTrackingService;
import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSign;
import com.metasoft.veyra.platform.health.domain.model.aggregates.VitalSignThreshold;
import com.metasoft.veyra.platform.health.domain.model.commands.ValidateVitalSignCommand;
import com.metasoft.veyra.platform.health.domain.model.events.VitalSignAnomalyDetectedEvent;
import com.metasoft.veyra.platform.health.domain.model.queries.GetVitalSignThresholdByResidentIdQuery;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.MeasurementId;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.SeverityLevel;
import com.metasoft.veyra.platform.health.domain.model.valueobjects.ValidationResult;
import com.metasoft.veyra.platform.health.domain.services.VitalSignCommandService;
import com.metasoft.veyra.platform.health.domain.services.VitalSignThresholdQueryService;
import com.metasoft.veyra.platform.health.infrastructure.persistence.jpa.repositories.VitalSignRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class VitalSignCommandServiceImpl implements VitalSignCommandService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VitalSignCommandServiceImpl.class);

    // Umbrales críticos siempre fijos (no configurables)
    private static final int HR_CRITICAL_LOW = 50;
    private static final int HR_CRITICAL_HIGH = 120;
    private static final int SPO2_CRITICAL = 90;
    private static final double TEMP_CRITICAL_LOW = 35.0;
    private static final double TEMP_CRITICAL_HIGH = 39.0;
    private static final int SYSTOLIC_CRITICAL = 180;
    private static final int DIASTOLIC_CRITICAL = 110;
    private static final int RR_CRITICAL_LOW = 8;
    private static final int RR_CRITICAL_HIGH = 24;

    private final VitalSignRepository vitalSignRepository;
    private final ExternalTrackingService externalTrackingService;
    private final VitalSignThresholdQueryService thresholdQueryService;
    private final ApplicationEventPublisher eventPublisher;

    public VitalSignCommandServiceImpl(
            VitalSignRepository vitalSignRepository,
            ExternalTrackingService externalTrackingService,
            VitalSignThresholdQueryService thresholdQueryService,
            ApplicationEventPublisher eventPublisher) {

        this.vitalSignRepository = vitalSignRepository;
        this.externalTrackingService = externalTrackingService;
        this.thresholdQueryService = thresholdQueryService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(ValidateVitalSignCommand command) {
        LOGGER.debug("Processing ValidateVitalSignCommand for device {}", command.deviceId());

        var residentIdOpt = externalTrackingService.fetchDeviceIdByResidentId(command.deviceId());

        if (residentIdOpt.isEmpty()) {
            LOGGER.warn("⚠️ Device {} not assigned to any resident - skipping validation",
                    command.deviceId());
            return;
        }

        var residentId = residentIdOpt.get();
        LOGGER.debug("Device {} → Resident {}", command.deviceId(), residentId.residentId());

        var thresholdOpt = thresholdQueryService
                .handle(new GetVitalSignThresholdByResidentIdQuery(residentId));

        var vitalSign = new VitalSign(
                residentId,
                new MeasurementId(command.measurementId())
        );

        if (thresholdOpt.isEmpty()) {
            LOGGER.info("Resident {} has no clinical parameters configured - storing as NORMAL without alert",
                    residentId.residentId());
            vitalSign.setSeverityLevel(SeverityLevel.NORMAL);
            vitalSignRepository.save(vitalSign);
            return;
        }

        var validationResult = validateVitalSigns(command, thresholdOpt.get());

        vitalSign.setSeverityLevel(validationResult.severity());
        vitalSignRepository.save(vitalSign);
        LOGGER.info("Created VitalSign {} for resident {} with severity {}",
                vitalSign.getId(), residentId.residentId(), validationResult.severity());

        if (validationResult.hasAnomalies()) {
            LOGGER.warn("⚠️ ANOMALY DETECTED for resident {}: {} - {}",
                    residentId.residentId(),
                    validationResult.severity(),
                    validationResult.getDetails());

            eventPublisher.publishEvent(new VitalSignAnomalyDetectedEvent(
                    this,
                    vitalSign.getId(),
                    residentId.residentId(),
                    command.measurementId(),
                    validationResult.severity(),
                    validationResult.getDetails()
            ));
        }
    }
    private ValidationResult validateVitalSigns(ValidateVitalSignCommand command, VitalSignThreshold t) {
        var anomalies = new ArrayList<String>();
        boolean hasCritical = false;

        int hrMin   = t.getHeartRateMin()         != null ? t.getHeartRateMin()         : 0;
        int hrMax   = t.getHeartRateMax()         != null ? t.getHeartRateMax()         : Integer.MAX_VALUE;
        int spo2Min = t.getOxygenSaturationMin()  != null ? t.getOxygenSaturationMin()  : 0;
        double tempMin = t.getTemperatureMin()    != null ? t.getTemperatureMin()        : 0.0;
        double tempMax = t.getTemperatureMax()    != null ? t.getTemperatureMax()        : Double.MAX_VALUE;
        int sysMax  = t.getSystolicMax()          != null ? t.getSystolicMax()           : Integer.MAX_VALUE;
        int diasMax = t.getDiastolicMax()         != null ? t.getDiastolicMax()          : Integer.MAX_VALUE;
        int rrMin   = t.getRespiratoryRateMin()   != null ? t.getRespiratoryRateMin()    : 0;
        int rrMax   = t.getRespiratoryRateMax()   != null ? t.getRespiratoryRateMax()    : Integer.MAX_VALUE;

        if (command.heartRate() != null) {
            if (command.heartRate() < hrMin) {
                anomalies.add(String.format("FC bajo: %d bpm (normal: %d-%d)", command.heartRate(), hrMin, hrMax));
                if (command.heartRate() < HR_CRITICAL_LOW) hasCritical = true;
            } else if (command.heartRate() > hrMax) {
                anomalies.add(String.format("FC alto: %d bpm (normal: %d-%d)", command.heartRate(), hrMin, hrMax));
                if (command.heartRate() > HR_CRITICAL_HIGH) hasCritical = true;
            }
        }

        if (command.oxygenSaturation() != null) {
            if (command.oxygenSaturation() < spo2Min) {
                anomalies.add(String.format("SpO2 bajo: %d%% (normal: >=%d%%)", command.oxygenSaturation(), spo2Min));
                if (command.oxygenSaturation() < SPO2_CRITICAL) hasCritical = true;
            }
        }

        if (command.temperature() != null) {
            if (command.temperature() < tempMin) {
                anomalies.add(String.format("Temperatura baja: %.1f°C (normal: %.1f-%.1f)", command.temperature(), tempMin, tempMax));
                if (command.temperature() < TEMP_CRITICAL_LOW) hasCritical = true;
            } else if (command.temperature() > tempMax) {
                anomalies.add(String.format("Temperatura alta: %.1f°C (normal: %.1f-%.1f)", command.temperature(), tempMin, tempMax));
                if (command.temperature() > TEMP_CRITICAL_HIGH) hasCritical = true;
            }
        }

        if (command.systolic() != null && command.diastolic() != null) {
            if (command.systolic() > sysMax || command.diastolic() > diasMax) {
                anomalies.add(String.format("PA elevada: %d/%d mmHg (normal: <%d/<%d)", command.systolic(), command.diastolic(), sysMax, diasMax));
                if (command.systolic() > SYSTOLIC_CRITICAL || command.diastolic() > DIASTOLIC_CRITICAL) hasCritical = true;
            }
        }

        if (command.respiratoryRate() != null) {
            if (command.respiratoryRate() < rrMin || command.respiratoryRate() > rrMax) {
                anomalies.add(String.format("FR anormal: %d rpm (normal: %d-%d)", command.respiratoryRate(), rrMin, rrMax));
                if (command.respiratoryRate() < RR_CRITICAL_LOW || command.respiratoryRate() > RR_CRITICAL_HIGH) hasCritical = true;
            }
        }

        SeverityLevel severity;
        if (hasCritical) severity = SeverityLevel.CRITICAL;
        else if (anomalies.size() >= 2) severity = SeverityLevel.HIGH;
        else if (anomalies.size() == 1) severity = SeverityLevel.MEDIUM;
        else severity = SeverityLevel.NORMAL;

        return new ValidationResult(severity, anomalies);
    }
}
