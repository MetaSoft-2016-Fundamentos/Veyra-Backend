package com.metasoft.veyra.platform.tracking.domain.model.aggregates;

import com.metasoft.veyra.platform.shared.domain.model.aggregates.AuditableMongoAggregateRoot;
import com.metasoft.veyra.platform.tracking.domain.model.valueobjects.*;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "measurements")
@Getter
public class Measurement extends AuditableMongoAggregateRoot<Measurement> {

    private final DeviceId deviceId;
    private final LocalDateTime timestamp;
    private final HeartRate heartRate;
    private final BloodPressure bloodPressure;
    private final Temperature temperature;
    private final OxygenSaturation oxygenSaturation;
    private final RespiratoryRate respiratoryRate;




    public Measurement(
            DeviceId deviceId,
            LocalDateTime timestamp,
            HeartRate heartRate,
            BloodPressure bloodPressure,
            Temperature temperature,
            OxygenSaturation oxygenSaturation,
            RespiratoryRate respiratoryRate) {

        this.deviceId = deviceId;
        this.timestamp = timestamp;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.oxygenSaturation = oxygenSaturation;
        this.respiratoryRate = respiratoryRate;
    }
}