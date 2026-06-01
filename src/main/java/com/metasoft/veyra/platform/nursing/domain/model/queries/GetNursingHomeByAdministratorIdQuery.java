package com.metasoft.veyra.platform.nursing.domain.model.queries;

public record GetNursingHomeByAdministratorIdQuery(Long administratorId) {
    public GetNursingHomeByAdministratorIdQuery{
        if (administratorId == null) {
            throw new IllegalArgumentException("administratorId cannot be null");
        }
        if (administratorId < 1) {
            throw new IllegalArgumentException("administratorId cannot be less than 1");
        }
    }
}
