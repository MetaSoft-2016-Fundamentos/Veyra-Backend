package com.metasoft.veyra.platform.health.domain.model.commands;

public record ValidateVitalSignCommand( String measurementId,
                                        String deviceId,
                                        Integer heartRate,
                                        Integer systolic,
                                        Integer diastolic,
                                        Double temperature,
                                        Integer oxygenSaturation,
                                        Integer respiratoryRate){
}
