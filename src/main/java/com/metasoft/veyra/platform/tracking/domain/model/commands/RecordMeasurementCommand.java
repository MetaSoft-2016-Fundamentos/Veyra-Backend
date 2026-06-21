package com.metasoft.veyra.platform.tracking.domain.model.commands;

public record RecordMeasurementCommand(Long deviceId,Double temperature,Integer heartRate,Integer oxygenSaturation) {
    public RecordMeasurementCommand {
        if (deviceId == null) {
            throw new IllegalArgumentException("deviceId cannot be null");
        }
        if (temperature == null) {
            throw new IllegalArgumentException("temperature cannot be null");
        }
        if (heartRate == null) {
            throw new IllegalArgumentException("heartRate cannot be null");
        }
        if (oxygenSaturation==null){
            throw new IllegalArgumentException("OxygenSaturation cannot be null");
        }
    }
}