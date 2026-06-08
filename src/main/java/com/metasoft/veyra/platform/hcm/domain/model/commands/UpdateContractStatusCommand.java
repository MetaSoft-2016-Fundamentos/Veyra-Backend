package com.metasoft.veyra.platform.hcm.domain.model.commands;

public record UpdateContractStatusCommand(Long staffMemberId,
                                          Long contractId, String newStatus) {
}
