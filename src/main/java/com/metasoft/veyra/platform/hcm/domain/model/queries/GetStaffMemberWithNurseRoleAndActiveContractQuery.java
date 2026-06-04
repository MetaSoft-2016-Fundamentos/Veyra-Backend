package com.metasoft.veyra.platform.hcm.domain.model.queries;

import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.NursingHomeId;

public record GetStaffMemberWithNurseRoleAndActiveContractQuery(Long staffId, NursingHomeId nursingHomeId) {
}