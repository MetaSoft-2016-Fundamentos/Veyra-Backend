package com.metasoft.veyra.platform.communication.infrastructure.providers.fcm.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.metasoft.veyra.platform.communication.application.internal.outboundservices.fcm.FirebaseMessagingGateway;
import com.metasoft.veyra.platform.communication.domain.exceptions.CommunicationProviderNotConfiguredException;
import com.metasoft.veyra.platform.communication.domain.exceptions.FirebaseMessagingIntegrationException;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationCommand;
import com.metasoft.veyra.platform.communication.infrastructure.providers.fcm.FirebasePushMessageBuilder;
import com.metasoft.veyra.platform.communication.infrastructure.providers.fcm.configuration.FirebaseMessagingSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@ConditionalOnProperty(name = "integrations.fcm.enabled", havingValue = "true")
public class FirebaseMessagingGatewayImpl implements FirebaseMessagingGateway {

    private final FirebaseMessagingSettings firebaseMessagingSettings;
    private final FirebaseMessaging firebaseMessaging;

    public FirebaseMessagingGatewayImpl(
            FirebaseMessagingSettings firebaseMessagingSettings,
            FirebaseMessaging firebaseMessaging
    ) {
        this.firebaseMessagingSettings = firebaseMessagingSettings;
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public void send(SendPushNotificationCommand command) {
        validateConfiguration();

        try {
            Message message = FirebasePushMessageBuilder.buildMessage(command);
            String messageId = firebaseMessaging.send(message);
            log.info("FCM push accepted (title={}, messageId={})", command.title(), messageId);
        } catch (FirebaseMessagingException exception) {
            log.error("FCM push rejected (title={}, errorCode={})", command.title(), exception.getMessagingErrorCode());
            throw new FirebaseMessagingIntegrationException("Firebase rejected push notification request", exception);
        }
    }

    private void validateConfiguration() {
        if (firebaseMessagingSettings.projectId() == null || firebaseMessagingSettings.projectId().isBlank()) {
            throw new CommunicationProviderNotConfiguredException("Firebase Cloud Messaging");
        }
        if (firebaseMessagingSettings.credentialsPath() == null || firebaseMessagingSettings.credentialsPath().isBlank()) {
            throw new CommunicationProviderNotConfiguredException("Firebase Cloud Messaging");
        }
    }
}
