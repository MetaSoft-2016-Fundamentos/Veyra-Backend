package com.metasoft.veyra.platform.communication.infrastructure.providers.fcm.configuration;

public record FirebaseMessagingSettings(
        String projectId,
        String credentialsPath
) {
}
