package com.metasoft.veyra.platform.iam.domain.model.commands;

public record SetPasswordCommand(String activationToken, String newPassword) {}