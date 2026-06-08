package com.metasoft.veyra.platform.hcm.domain.model.queries;

public record GetActiveContractByStaffMemberIdQuery(Long staffId) {
    public GetActiveContractByStaffMemberIdQuery {
        if (staffId==null|| staffId<=0){
            throw new IllegalArgumentException("StaffId cannot be null or less then or equal to 0");
        }
    }
}
