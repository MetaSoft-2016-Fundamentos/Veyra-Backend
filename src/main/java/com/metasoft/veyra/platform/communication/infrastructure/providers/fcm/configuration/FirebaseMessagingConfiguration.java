package com.metasoft.veyra.platform.communication.infrastructure.providers.fcm.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class FirebaseMessagingConfiguration {

    @Value("${firebase.project.id:}")
    private String projectId;

    @Value("${firebase.credentials.path:}")
    private String credentialsPath;

    @Bean
    FirebaseMessagingSettings firebaseMessagingSettings() {
        return new FirebaseMessagingSettings(projectId, credentialsPath);
    }

    @Bean
    @ConditionalOnProperty(name = "integrations.fcm.enabled", havingValue = "true")
    FirebaseApp firebaseApp(FirebaseMessagingSettings firebaseMessagingSettings) throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(loadCredentials(firebaseMessagingSettings.credentialsPath()))
                .setProjectId(firebaseMessagingSettings.projectId())
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    @ConditionalOnProperty(name = "integrations.fcm.enabled", havingValue = "true")
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    private GoogleCredentials loadCredentials(String credentialsPath) throws IOException {
        Path serviceAccountPath = Path.of(credentialsPath);
        if (!Files.isRegularFile(serviceAccountPath)) {
            throw new IllegalStateException("Firebase service account file not found");
        }

        try (InputStream credentialsStream = Files.newInputStream(serviceAccountPath)) {
            return GoogleCredentials.fromStream(credentialsStream);
        }
    }
}
