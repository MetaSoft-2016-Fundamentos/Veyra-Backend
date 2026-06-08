package com.metasoft.veyra.platform.nursing.domain.model.commands;

public record CreateARoomToTheNursingHomeCommand(Long nursingHomeId, Integer capacity,
                                                 String type,String roomNumber) {
}
