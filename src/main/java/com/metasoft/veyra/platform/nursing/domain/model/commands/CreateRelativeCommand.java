package com.metasoft.veyra.platform.nursing.domain.model.commands;

public record CreateRelativeCommand( String firstname,String lastname,String email,Long residentId,Long nursingHomeId) {
}
