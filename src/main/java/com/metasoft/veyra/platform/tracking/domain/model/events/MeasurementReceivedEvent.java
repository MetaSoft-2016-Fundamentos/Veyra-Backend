package com.metasoft.veyra.platform.tracking.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
@Getter
public class MeasurementReceivedEvent  extends ApplicationEvent {
   private final  String        measurementId;
   private final  String        deviceId;
   private final  LocalDateTime measurementTimestamp;
   private final  Integer       heartRate;
   private final  Integer       systolic;
   private final  Integer       diastolic;
   private final  Double        temperature;
   private final  Integer       oxygenSaturation;
   private final  Integer       respiratoryRate;

    public MeasurementReceivedEvent(Object source,String measurementId1, String deviceId1, LocalDateTime timestamp1, Integer heartRate1, Integer systolic1, Integer diastolic1, Double temperature1, Integer oxygenSaturation1, Integer respiratoryRate1) {
        super(source);
        this.measurementId = measurementId1;
        this.deviceId = deviceId1;
        this.measurementTimestamp = timestamp1;
        this.heartRate = heartRate1;
        this.systolic = systolic1;
        this.diastolic = diastolic1;
        this.temperature = temperature1;
        this.oxygenSaturation = oxygenSaturation1;
        this.respiratoryRate = respiratoryRate1;
    }
}
