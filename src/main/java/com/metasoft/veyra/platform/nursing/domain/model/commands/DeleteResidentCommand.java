package com.metasoft.veyra.platform.nursing.domain.model.commands;

public record DeleteResidentCommand(Long residentId) {
    public DeleteResidentCommand{
        if (residentId == null||residentId<=0) {
            throw new IllegalArgumentException(" residentId cannot be null or less then 1");
        }
    }
}
